package comeon;

import in.yuvi.http.fluent.ProgressListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comeon.commons.Commons;
import comeon.commons.CommonsImpl;
import comeon.model.Picture;
import comeon.model.Template;

public final class Core {
  private static final Logger LOGGER = LoggerFactory.getLogger(Core.class);
  
  private static final Core INSTANCE = new Core();

  private final List<Picture> pictures;

  private final ExecutorService pool;

  private final Users users;

  private final Templates templates;

  private Core() {
    this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    this.pictures = new ArrayList<>();
    this.users = new Users();
    this.templates = new Templates();
  }

  public void addPictures(final File[] files, final Template defautTemplate) throws UserNotSetException {
    final Pictures picturesReader = new Pictures(files, defautTemplate, pool);
    final List<Picture> newPictures = picturesReader.readFiles(users.getUser()).getPictures();
    this.pictures.addAll(newPictures);
  }

  public List<Picture> getPictures() {
    return pictures;
  }

  public void uploadPictures(final UploadMonitor monitor) {
    final List<Picture> batch = new ArrayList<>(this.pictures);
    monitor.setBatchSize(batch.size());
    this.pool.submit(new Runnable() {
      @Override
      public void run() {
        try {
          final Commons commons = new CommonsImpl(users.getUser());
          monitor.uploadStarting();
          int index = 0;
          for (final Picture picture : batch) {
            try {
              final ProgressListener listener = monitor.itemStarting(index, picture.getFile().length(), picture.getFileName());
              commons.upload(picture, listener);
            } catch (final NotLoggedInException | FailedLoginException | FailedUploadException | IOException e) {
              LOGGER.warn("Picture upload failed", e);
            } finally {
              monitor.itemDone(index);
              index++;
            }
          }
          commons.logout();
          monitor.uploadDone();
        } catch (final UserNotSetException | FailedLogoutException e) {
          LOGGER.warn("Batch upload failed", e);
        }
      }
    });
  }

  public Users getUsers() {
    return users;
  }

  public Templates getTemplates() {
    return templates;
  }

  public static Core getInstance() {
    return INSTANCE;
  }

  public ExecutorService getPool() {
    return pool;
  }
}
