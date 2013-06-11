package comeon.core;

import in.yuvi.http.fluent.ProgressListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import comeon.commons.Commons;
import comeon.commons.FailedLoginException;
import comeon.commons.FailedLogoutException;
import comeon.commons.FailedUploadException;
import comeon.commons.NotLoggedInException;
import comeon.model.Picture;
import comeon.model.Template;
import comeon.pictures.Pictures;
import comeon.pictures.PicturesImpl;
import comeon.users.UserNotSetException;
import comeon.users.Users;

public final class CoreImpl implements Core {
  private static final Logger LOGGER = LoggerFactory.getLogger(CoreImpl.class);
  
  private final List<Picture> pictures;

  private final ExecutorService pool;

  private final Users users;

  private final Commons commons;
  
  @Inject
  private CoreImpl(final Users users, final Commons commons, final ExecutorService pool) {
    this.pictures = new ArrayList<>();
    this.users = users;
    this.pool = pool;
    this.commons = commons;
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
        } catch (final FailedLogoutException e) {
          // TODO i18n
          LOGGER.warn("Couldn't close Commons session properly", e);
        }
      }
    });
  }
}
