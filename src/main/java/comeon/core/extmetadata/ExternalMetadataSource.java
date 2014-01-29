package comeon.core.extmetadata;

import comeon.model.Picture;

public interface ExternalMetadataSource<T> {
  void loadMetadata();
  T getPictureMetadata(Picture picture);
}
