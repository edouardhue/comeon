package comeon;

import java.io.File;
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
  
  public void addPictures(final File[] files, final Template defautTemplate) {
    final Pictures pictures = new Pictures(files, defautTemplate, pool);
    this.pictures.addAll(pictures.readFiles(users.getUser()).getPictures());
  }
  
  public List<Picture> getPictures() {
    return pictures;
  }
  
  public static Core getInstance() {
    return INSTANCE;
  }
  
  ExecutorService getPool() {
    return pool;
  }
  
}
