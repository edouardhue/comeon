package comeon.core;

import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.model.Media;
import comeon.model.Template;

import java.io.File;
import java.util.List;

public interface Core {

    String EXTERNAL_METADATA_KEY = "external";

    String[] PICTURE_EXTENSIONS = { "jpg", "jpeg" };

    String[] AUDIO_EXTENSIONS = { "ogg", "flac", "wav" };

    void addMedia(File[] files, Template defautTemplate, ExternalMetadataSource<?> externalMetadataSource);

    void removeMedia(Media media);

    void removeAllMedia();

    List<Media> getMedia();

    int countMediaToBeUploaded();

    void uploadMedia();

    void abort();
}