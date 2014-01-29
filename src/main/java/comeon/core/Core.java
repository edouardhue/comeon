package comeon.core;

import java.io.File;
import java.util.List;

import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.model.Picture;
import comeon.model.Template;

public interface Core {

  String EXTERNAL_METADATA_KEY = "external";

  void addPictures(File[] files, Template defautTemplate, ExternalMetadataSource<?> externalMetadataSource);

  void removePicture(Picture picture);
  
  List<Picture> getPictures();

  void uploadPictures(UploadMonitor monitor);

}