package comeon.core.mediareaders;

import comeon.core.Core;
import comeon.core.MediaUploadBatch;
import comeon.model.Media;
import comeon.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

abstract class AbstractMediaReader implements MediaReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMediaReader.class);

    private final File file;

    private final User user;

    AbstractMediaReader(final File file, final User user) {
        this.file = file;
        this.user = user;
    }

    protected final File getFile() {
        return file;
    }

    @Override
    public final Optional<Media> readMedia(final MediaUploadBatch context) {
        final String fileName = file.getAbsolutePath();
        try {
            final Media media = buildMedia(context);
            media.getMetadata().put(Core.EXTERNAL_METADATA_KEY, context.getExternalMetadata(media, media.getMetadata()));
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
            return Optional.of(media);
        } catch (final MediaReaderException e) {
            LOGGER.warn("Can't read metadata from {}", fileName, e);
            return Optional.empty();
        } catch (final IOException e) {
            LOGGER.warn("Can't read file {}", fileName, e);
            return Optional.empty();
        }
    }

    protected abstract Media buildMedia(final MediaUploadBatch context) throws MediaReaderException, IOException;
}