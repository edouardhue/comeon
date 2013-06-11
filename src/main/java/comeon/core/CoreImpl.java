package comeon.core;

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
import comeon.commons.FailedLoginException;
import comeon.commons.FailedLogoutException;
import comeon.commons.FailedUploadException;
import comeon.commons.NotLoggedInException;
import comeon.model.Picture;
import comeon.model.Template;
import comeon.pictures.Pictures;
import comeon.pictures.PicturesImpl;
import comeon.templates.velocity.Templates;
import comeon.templates.velocity.TemplatesImpl;
import comeon.users.UserNotSetException;
import comeon.users.Users;
import comeon.users.UsersImpl;

public final class CoreImpl implements Core {
  private static final Logger LOGGER = LoggerFactory.getLogger(CoreImpl.class);
  
  private static final CoreImpl INSTANCE = new CoreImpl();

  private final List<Picture> pictures;

  private final ExecutorService pool;

  private final Users users;

  private final Templates templates;

  private CoreImpl() {
    this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    this.pictures = new ArrayList<>();
    this.users = new UsersImpl();
    this.templates = new TemplatesImpl();
  }

  /* (non-Javadoc)
   * @see comeon.Core#addPictures(java.io.File[], comeon.model.Template)
   */
  @Override
  public void addPictures(final File[] files, final Template defautTemplate) throws UserNotSetException {
    final Pictures picturesReader = new PicturesImpl(files, defautTemplate, pool);
    final List<Picture> newPictures = picturesReader.readFiles(users.getUser()).getPictures();
    this.pictures.addAll(newPictures);
  }

  /* (non-Javadoc)
   * @see comeon.Core#getPictures()
   */
  @Override
  public List<Picture> getPictures() {
    return pictures;
  }

  /* (non-Javadoc)
   * @see comeon.Core#uploadPictures(comeon.UploadMonitor)
   */
  @Override
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

  public static CoreImpl getInstance() {
    return INSTANCE;
  }

  public ExecutorService getPool() {
    return pool;
  }
}
