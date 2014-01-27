package comeon.core;

import comeon.model.Picture;

public final class NullMetadataSource implements ExternalMetadataSource<Void> {
  @Override
  public Void getPictureMetadata(Picture picture) {
    return null;
  }

}
