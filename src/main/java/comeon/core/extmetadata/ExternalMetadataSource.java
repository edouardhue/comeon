package comeon.core.extmetadata;

import java.util.Map;

import comeon.model.Picture;

public interface ExternalMetadataSource<T> {
  void loadMetadata();
  T getPictureMetadata(Picture picture, Map<String, Object> pictureMetadata);
}
