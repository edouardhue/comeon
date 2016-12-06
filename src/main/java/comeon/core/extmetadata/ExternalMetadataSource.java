package comeon.core.extmetadata;

import comeon.model.Media;

import java.io.IOException;
import java.util.Map;

public interface ExternalMetadataSource<T> {
    void loadMetadata() throws IOException, DuplicateKeyException;

    T getMediaMetadata(Media media, Map<String, Object> mediaMetadata);
}
