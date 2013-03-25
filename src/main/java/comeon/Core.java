package comeon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comeon.model.Picture;
import comeon.model.Template;

public final class Core {
  private static final Core INSTANCE = new Core();
  
  private List<Picture> pictures;
  
  private ExecutorService pool;
  
  private Users users;
  
  private Core() {
    this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    this.pictures = new ArrayList<>();
    this.users = new Users();
  }
  
  public void addPictures(final File[] files, final Template defautTemplate) throws UserNotSetException {
    final Pictures picturesReader = new Pictures(files, defautTemplate, pool);
    final List<Picture> newPictures = picturesReader.readFiles(users.getUser()).getPictures();
    this.pictures.addAll(newPictures);
  }
  
  public List<Picture> getPictures() {
    return pictures;
  }
  
  public void uploadPictures() throws UserNotSetException, NotLoggedInException, FailedLoginException, FailedUploadException, IOException, FailedLogoutException {
    final Commons commons = new Commons(users.getUser());
    for (final Picture picture : this.pictures) {
      commons.upload(picture);
    }
    commons.logout();
  }
  
  public Users getUsers() {
    return users;
  }
  
  public static Core getInstance() {
    return INSTANCE;
  }
  
  public ExecutorService getPool() {
    return pool;
  }
}
