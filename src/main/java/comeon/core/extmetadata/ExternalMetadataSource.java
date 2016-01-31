package comeon.core.extmetadata;

import java.util.Map;

import comeon.model.Media;

public interface ExternalMetadataSource<T> {
  void loadMetadata();
  T getMediaMetadata(Media media, Map<String, Object> mediaMetadata);
}
