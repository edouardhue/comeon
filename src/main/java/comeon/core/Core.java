package comeon.core;

import java.io.File;
import java.util.List;

import comeon.UploadMonitor;
import comeon.model.Picture;
import comeon.model.Template;
import comeon.users.UserNotSetException;

public interface Core {

  public abstract void addPictures(File[] files, Template defautTemplate) throws UserNotSetException;

  public abstract List<Picture> getPictures();

  public abstract void uploadPictures(UploadMonitor monitor);

}