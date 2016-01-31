package comeon.core;

import java.io.File;
import java.util.List;

import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.model.Media;
import comeon.model.Template;

public interface Core {

  String EXTERNAL_METADATA_KEY = "external";

  void addMedia(File[] files, Template defautTemplate, ExternalMetadataSource<?> externalMetadataSource);

  void removeMedia(Media media);
  
  List<Media> getMedia();

  int countMediaToBeUploaded();
  
  void uploadMedia();

  void abort();
}