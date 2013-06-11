package comeon.core;

import java.io.File;
import java.util.List;

import comeon.UploadMonitor;
import comeon.UserNotSetException;
import comeon.model.Picture;
import comeon.model.Template;

public interface Core {

  public abstract void addPictures(File[] files, Template defautTemplate) throws UserNotSetException;

  public abstract List<Picture> getPictures();

  public abstract void uploadPictures(UploadMonitor monitor);

}