package comeon.core;

import java.io.File;
import java.util.List;

import comeon.model.Picture;
import comeon.model.Template;

public interface Core {

  void addPictures(File[] files, Template defautTemplate);

  void removePicture(Picture picture);
  
  List<Picture> getPictures();

  void uploadPictures(UploadMonitor monitor);

}