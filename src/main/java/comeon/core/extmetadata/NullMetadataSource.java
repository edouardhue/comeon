package comeon.core.extmetadata;

import comeon.model.Picture;

public final class NullMetadataSource implements ExternalMetadataSource<Void> {
  @Override
  public void loadMetadata() {
  }

  @Override
  public Void getPictureMetadata(Picture picture) {
    return null;
  }
}
