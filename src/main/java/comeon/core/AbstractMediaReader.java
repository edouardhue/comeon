package comeon.core;

import comeon.model.Media;
import comeon.model.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

abstract class AbstractMediaReader implements Runnable {

    private final MediaUploadBatch mediaUploadBatch;

    private final File file;

    private final User user;

    AbstractMediaReader(final MediaUploadBatch mediaUploadBatch, final File file, final User user) {
        this.mediaUploadBatch = mediaUploadBatch;
        this.file = file;
        this.user = user;
    }

    protected final MediaUploadBatch getMediaUploadBatch() {
        return mediaUploadBatch;
    }

    protected final File getFile() {
        return file;
    }

    @Override
    public final void run() {
        final String fileName = file.getAbsolutePath();
        try {
            final Media media = buildMedia();
            media.getMetadata().put(Core.EXTERNAL_METADATA_KEY, getMediaUploadBatch().getMediaMetadata(media, media.getMetadata()));
            media.renderTemplate(user);
            media.addPropertyChangeListener(evt -> {
                if ("templateText".equals(evt.getPropertyName())) {
                    mediaUploadBatch.execute(() -> media.renderTemplate(user));
                }
            });
            mediaUploadBatch.add(media);
        } catch (final MediaReaderException e) {
            MediaUploadBatch.LOGGER.warn("Can't read metadata from {}", fileName, e);
        } catch (final IOException e) {
            MediaUploadBatch.LOGGER.warn("Can't read file {}", fileName, e);
        } finally {
            mediaUploadBatch.decreaseLatch();
        }
    }

    protected abstract Media buildMedia() throws MediaReaderException, IOException;
}