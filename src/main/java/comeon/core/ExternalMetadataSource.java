package comeon.core;

import comeon.model.Picture;

public interface ExternalMetadataSource<T> {
  T getPictureMetadata(Picture picture);
}
