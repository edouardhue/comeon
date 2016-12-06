package comeon.core;

import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.model.Media;
import comeon.model.Template;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public interface Core {

    String EXTERNAL_METADATA_KEY = "external";

    String[] PICTURE_EXTENSIONS = { "jpg", "jpeg" };

    String[] AUDIO_EXTENSIONS = { "ogg", "flac", "wav" };

    void addMedia(final File[] files, final Template defautTemplate, final ExternalMetadataSource<?> externalMetadataSource) throws IOException;

    void removeMedia(Media media);

    void removeAllMedia();

    Set<Media> getMedia();

    int countMediaToBeUploaded();

    void uploadMedia();

    void abort();
}