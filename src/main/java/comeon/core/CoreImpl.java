package comeon.core;

import in.yuvi.http.fluent.ProgressListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.mediawiki.FailedLoginException;
import comeon.mediawiki.FailedLogoutException;
import comeon.mediawiki.FailedUploadException;
import comeon.mediawiki.MediaWiki;
import comeon.mediawiki.MediaWikiFactory;
import comeon.mediawiki.NotLoggedInException;
import comeon.model.Picture;
import comeon.model.Picture.State;
import comeon.model.Template;
import comeon.model.Wiki;
import comeon.ui.actions.PictureRemovedEvent;
import comeon.ui.actions.PicturesAddedEvent;
import comeon.wikis.ActiveWikiChangeEvent;
import comeon.wikis.Wikis;

@Singleton
public final class CoreImpl implements Core {
  private static final Logger LOGGER = LoggerFactory.getLogger(CoreImpl.class);

  private final List<Picture> pictures;

  private final ExecutorService pool;

  private final Wikis wikis;

  private final EventBus bus;

  private final PicturesBatchFactory picturesBatchFactory;
  
  private final MediaWikiFactory mediaWikiFactory;

  private MediaWiki activeMediaWiki;

  @Inject
  private CoreImpl(final Wikis wikis, final ExecutorService pool, final EventBus bus,
      final PicturesBatchFactory picturesBatchFactory, final MediaWikiFactory mediaWikiFactory) {
    this.pictures = new ArrayList<>();
    this.pool = pool;
    this.bus = bus;
    this.wikis = wikis;
    this.picturesBatchFactory = picturesBatchFactory;
    this.mediaWikiFactory = mediaWikiFactory;
    final Wiki activeWiki = wikis.getActiveWiki();
    if (activeWiki == null) {
      throw new IllegalStateException("There must be one active wiki.");
    } else {
      this.activeMediaWiki = mediaWikiFactory.build(activeWiki);
    }
  }

  @Override
  public void addPictures(final File[] files, final Template defautTemplate,
      final ExternalMetadataSource<?> externalMetadataSource) {
    externalMetadataSource.loadMetadata();
    final PicturesBatch picturesReader = picturesBatchFactory.makePicturesBatch(files, defautTemplate,
        externalMetadataSource);
    final List<Picture> newPictures = picturesReader.readFiles(wikis.getActiveWiki().getUser()).getPictures();
    this.pictures.addAll(newPictures);
    bus.post(new PicturesAddedEvent());
  }

  @Override
  public void removePicture(final Picture picture) {
    pictures.remove(picture);
    bus.post(new PictureRemovedEvent());
  }

  @Override
  public List<Picture> getPictures() {
    return pictures;
  }

  private boolean shouldUpload(final Picture picture) {
    return !State.UploadedSuccessfully.equals(picture.getState());
  }

  @Override
  public int countPicturesToBeUploaded() {
    int picturesToBeUploaded = 0;
    for (final Picture picture : pictures) {
      if (shouldUpload(picture)) {
        picturesToBeUploaded++;
      }
    }
    return picturesToBeUploaded;
  }

  private class UploadTask implements Callable<Void> {
    private final int index;
    
    private final Picture picture;
    
    private final UploadMonitor monitor;
    
    public UploadTask(final int index, final Picture picture, final UploadMonitor monitor) {
      this.index = index;
      this.picture = picture;
      this.monitor = monitor;
    }
    
    @Override
    public Void call() throws Exception {
      try {
        final ProgressListener listener = monitor.itemStarting(index, picture.getFile().length(), picture.getFileName());
        activeMediaWiki.upload(picture, listener);
        picture.setState(State.UploadedSuccessfully);
        monitor.itemDone(index);
      } catch (final NotLoggedInException | FailedLoginException | FailedUploadException | IOException e) {
        LOGGER.warn("Picture upload failed", e);
        picture.setState(State.FailedUpload);
        monitor.itemFailed(index, e);
      }
      return null;
    }
  }
  
  @Override
  public void uploadPictures(final UploadMonitor monitor) {
    final int picturesToBeUploaded = this.countPicturesToBeUploaded();
    monitor.setBatchSize(picturesToBeUploaded);
    final List<UploadTask> tasks = new ArrayList<>(picturesToBeUploaded);
    int counter = 0;
    for (final Picture picture : pictures) {
      if (shouldUpload(picture)) {
        tasks.add(new UploadTask(counter, picture, monitor));
        counter++;
      }
    }
    monitor.uploadStarting();
    try {
      final List<Future<Void>> results = pool.invokeAll(tasks);
      for (final Future<Void> result : results) {
        try {
          result.get();
        } catch (final ExecutionException e) {
          LOGGER.warn("Task execution failed", e);
        }
      }
    } catch (final InterruptedException e) {
      Thread.interrupted();
    } finally {
      try {
        activeMediaWiki.logout();
      } catch (final FailedLogoutException e) {
        // TODO i18n
        LOGGER.warn("Couldn't close Mediawiki session properly", e);
      }
      monitor.uploadDone();
    }
  }
  
  @Subscribe
  public void handleActiveWikiChangeEvent(final ActiveWikiChangeEvent event) {
    if (this.activeMediaWiki.isLoggedIn()) {
      try {
        this.activeMediaWiki.logout();
      } catch (final FailedLogoutException e) {
        // TODO i18n
        LOGGER.warn("Failed implicit logout", e);
      }
    }
    this.activeMediaWiki = mediaWikiFactory.build(wikis.getActiveWiki());
  }
}
