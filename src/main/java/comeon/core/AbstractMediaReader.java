package comeon.core;

import comeon.model.Media;
import comeon.model.User;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

abstract class AbstractMediaReader {

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

    public final void readFile() {
        final String fileName = file.getAbsolutePath();
        try {
            final Media media = buildMedia();
            media.getMetadata().put(Core.EXTERNAL_METADATA_KEY, getMediaUploadBatch().getMediaMetadata(media, media.getMetadata()));
            media.renderTemplate(user);
            media.addPropertyChangeListener(evt -> {
                if ("templateText".equals(evt.getPropertyName())) {
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            media.renderTemplate(user);
                            return null;
                        }
                    }.execute();
                }
            });
            mediaUploadBatch.add(media);
        } catch (final MediaReaderException e) {
            MediaUploadBatch.LOGGER.warn("Can't read metadata from {}", fileName, e);
        } catch (final IOException e) {
            MediaUploadBatch.LOGGER.warn("Can't read file {}", fileName, e);
        }
    }

    protected abstract Media buildMedia() throws MediaReaderException, IOException;
}