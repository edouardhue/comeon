package comeon.core.extmetadata;

import comeon.model.Media;

import java.util.Map;

public interface ExternalMetadataSource<T> {
    void loadMetadata();

    T getMediaMetadata(Media media, Map<String, Object> mediaMetadata);
}
