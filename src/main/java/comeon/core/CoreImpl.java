package comeon.core;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.events.*;
import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.mediawiki.*;
import comeon.model.Media;
import comeon.model.Media.State;
import comeon.model.Template;
import comeon.model.Wiki;
import comeon.ui.actions.MediaAddedEvent;
import comeon.ui.actions.MediaRemovedEvent;
import comeon.wikis.ActiveWikiChangeEvent;
import comeon.wikis.Wikis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Singleton
public final class CoreImpl implements Core {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreImpl.class);

    private final List<Media> media;

    private final ExecutorService pool;

    private final Wikis wikis;

    private final EventBus bus;

    private final MediaUploadBatchFactory mediaUploadBatchFactory;

    private final MediaWikiFactory mediaWikiFactory;

    private final Queue<Future<Void>> currentTasks;

    private MediaWiki activeMediaWiki;

    @Inject
    private CoreImpl(final Wikis wikis, final ExecutorService pool, final EventBus bus,
                     final MediaUploadBatchFactory mediaUploadBatchFactory, final MediaWikiFactory mediaWikiFactory) {
        this.media = new ArrayList<>();
        this.currentTasks = new ConcurrentLinkedQueue<>();
        this.pool = pool;
        this.bus = bus;
        this.wikis = wikis;
        this.mediaUploadBatchFactory = mediaUploadBatchFactory;
        this.mediaWikiFactory = mediaWikiFactory;
        final Wiki activeWiki = wikis.getActiveWiki();
        if (activeWiki == null) {
            throw new IllegalStateException("There must be one active wiki.");
        } else {
            this.activeMediaWiki = mediaWikiFactory.build(activeWiki);
        }
    }

    @Override
    public void addMedia(final File[] files, final Template defautTemplate,
                         final ExternalMetadataSource<?> externalMetadataSource) {
        externalMetadataSource.loadMetadata();
        final MediaUploadBatch mediaReader = mediaUploadBatchFactory.makeMediaUploadBatch(files, defautTemplate,
                externalMetadataSource);
        final List<Media> newMedia = mediaReader.readFiles(wikis.getActiveWiki().getUser()).getMedia();
        this.media.addAll(newMedia);
        bus.post(new MediaAddedEvent(Collections.unmodifiableList(newMedia)));
    }

    @Override
    public void removeMedia(final Media media) {
        this.media.remove(media);
        bus.post(new MediaRemovedEvent(media));
    }

    @Override
    public void removeAllMedia() {
        final List<Media> removedMedia = new ArrayList<>(this.media);
        this.media.clear();
        bus.post(new MediaRemovedEvent(removedMedia));
    }

    @Override
    public List<Media> getMedia() {
        return Collections.unmodifiableList(media);
    }

    private boolean shouldUpload(final Media media) {
        return !State.UploadedSuccessfully.equals(media.getState());
    }

    @Override
    public int countMediaToBeUploaded() {
        return (int) media.parallelStream().filter(this::shouldUpload).count();
    }

    private class UploadTask implements Callable<Void> {
        private final Logger taskLogger = LoggerFactory.getLogger(UploadTask.class);

        private final Media media;

        public UploadTask(final Media media) {
            this.media = media;
        }

        @Override
        public Void call() throws Exception {
            try {
                taskLogger.debug("Starting upload of {}", media.getFileName());
                final ProgressListenerAdapter progressListener = new ProgressListenerAdapter();
                bus.post(new MediaTransferStartingEvent(media, progressListener));
                activeMediaWiki.upload(media, progressListener);
                media.setState(State.UploadedSuccessfully);
                bus.post(new MediaTransferDoneEvent(media));
                taskLogger.debug("Finished upload of {}", media.getFileName());
            } catch (final NotLoggedInException | FailedLoginException | FailedUploadException | IOException e) {
                taskLogger.warn("Failed upload of {}", media.getFileName(), e);
                media.setState(State.FailedUpload);
                bus.post(new MediaTransferFailedEvent(media, e));
            }
            return null;
        }

        @Override
        public boolean equals(final Object obj) {
            final boolean isEqual;
            if (obj == null) {
                isEqual = false;
            } else if (obj instanceof UploadTask) {
                final UploadTask task = (UploadTask) obj;
                isEqual = task.media.equals(this.media);
            } else {
                isEqual = super.equals(obj);
            }
            return isEqual;
        }
    }

    @Override
    public void uploadMedia() {
        final List<Media> mediaToBeUploaded = media.parallelStream().filter(this::shouldUpload).collect(Collectors.toList());
        LOGGER.info("Uploading {} media to {}.", mediaToBeUploaded.size(), activeMediaWiki.getName());
        bus.post(new UploadStartingEvent(mediaToBeUploaded));
        final List<Future<Void>> tasks = mediaToBeUploaded.parallelStream().map(media -> pool.submit(new UploadTask(media))).collect(Collectors.toList());
        currentTasks.addAll(tasks);
        try {
            for (final Future<Void> task : tasks) {
                try {
                    task.get();
                } catch (final CancellationException e) {
                    LOGGER.debug("Task was cancelled", e);
                } catch (final ExecutionException e) {
                    LOGGER.warn("Task execution failed", e);
                } finally {
                    currentTasks.remove(task);
                }
            }
        } catch (final InterruptedException e) {
            Thread.interrupted();
            LOGGER.warn("We were interrupted while waiting for uploads to complete", e);
        } finally {
            try {
                activeMediaWiki.logout();
            } catch (final FailedLogoutException e) {
                LOGGER.warn("Couldn't close Mediawiki session properly", e);
            }
            bus.post(new UploadDoneEvent());
            LOGGER.info("Upload done.");
        }
    }

    @Override
    public void abort() {
        final List<Future<Void>> cancelledTasks = currentTasks.parallelStream().filter(task -> task.cancel(true)).collect(Collectors.toList());
        currentTasks.removeAll(cancelledTasks);
    }

    @Subscribe
    public void handleActiveWikiChangeEvent(final ActiveWikiChangeEvent event) {
        if (this.activeMediaWiki.isLoggedIn()) {
            try {
                this.activeMediaWiki.logout();
            } catch (final FailedLogoutException e) {
                LOGGER.warn("Failed implicit logout", e);
            }
        }
        this.activeMediaWiki = mediaWikiFactory.build(wikis.getActiveWiki());
        for (final Media media : this.media) {
            media.renderTemplate(wikis.getActiveWiki().getUser());
        }
    }
}
