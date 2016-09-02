package comeon.core.extmetadata;

import comeon.model.Media;

import java.util.Map;

public final class NullMetadataSource implements ExternalMetadataSource<Void> {
    @Override
    public void loadMetadata() {
    }

    @Override
    public Void getMediaMetadata(Media media, final Map<String, Object> mediaMetadata) {
        return null;
    }
}
