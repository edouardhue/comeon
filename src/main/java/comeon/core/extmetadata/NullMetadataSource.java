package comeon.core.extmetadata;

import java.util.Map;

import comeon.model.Picture;

public final class NullMetadataSource implements ExternalMetadataSource<Void> {
  @Override
  public void loadMetadata() {
  }

  @Override
  public Void getPictureMetadata(Picture picture, final Map<String, Object> pictureMetadata) {
    return null;
  }
}
