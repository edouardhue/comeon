package comeon.core.extmetadata;

import java.util.Map;

import comeon.model.Media;

public final class NullMetadataSource implements ExternalMetadataSource<Void> {
  @Override
  public void loadMetadata() {
  }

  @Override
  public Void getMediaMetadata(Media media, final Map<String, Object> mediaMetadata) {
    return null;
  }
}
