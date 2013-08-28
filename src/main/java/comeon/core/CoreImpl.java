package comeon.core;

import in.yuvi.http.fluent.ProgressListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import comeon.mediawiki.FailedLoginException;
import comeon.mediawiki.FailedLogoutException;
import comeon.mediawiki.FailedUploadException;
import comeon.mediawiki.MediaWiki;
import comeon.mediawiki.NotLoggedInException;
import comeon.model.Picture;
import comeon.model.Template;
import comeon.ui.actions.PicturesAddedEvent;
import comeon.users.UserNotSetException;
import comeon.users.Users;

@Singleton
public final class CoreImpl implements Core {
  private static final Logger LOGGER = LoggerFactory.getLogger(CoreImpl.class);

  private final List<Picture> pictures;

  private final ExecutorService pool;

  private final Users users;

  private final MediaWiki commons;

  private final EventBus bus;

  @Inject
  private CoreImpl(final Users users, final MediaWiki commons, final ExecutorService pool, final EventBus bus) {
    this.pictures = new ArrayList<>();
    this.users = users;
    this.pool = pool;
    this.commons = commons;
    this.bus = bus;
  }

  @Override
  public void addPictures(final File[] files, final Template defautTemplate) throws UserNotSetException {
    final Pictures picturesReader = new Pictures(files, defautTemplate, pool);
    final List<Picture> newPictures = picturesReader.readFiles(users.getUser()).getPictures();
    this.pictures.addAll(newPictures);
    bus.post(new PicturesAddedEvent());
  }

  @Override
  public List<Picture> getPictures() {
    return pictures;
  }

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
              final ProgressListener listener = monitor.itemStarting(index, picture.getFile().length(),
                  picture.getFileName());
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
