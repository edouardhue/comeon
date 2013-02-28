package comeon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import comeon.model.Picture;
import comeon.model.Template;
import comeon.model.User;

final class Pictures {
  private static final Logger LOGGER = LoggerFactory.getLogger(Pictures.class);
  
  private final File[] files;
  
  private final Template defaultTemplate;
  
  private final List<Picture> pictures;

  private final ExecutorService pool;
  
  private final CountDownLatch latch;
  
  Pictures(final File[] files, final Template defautTemplate, final ExecutorService pool) {
    this.files = files;
    this.defaultTemplate = defautTemplate;
    this.pool = pool;
    this.pictures = Collections.synchronizedList(new ArrayList<Picture>(files.length));
    this.latch = new CountDownLatch(files.length);
  }
  
  Pictures readFiles(final User user) {
    for (final File file : files) {
      pool.execute(new PictureReader(file, user));
    }
    
    try {
      latch.await();
    } catch (final InterruptedException e) {
      Thread.interrupted();
    }
    
    return this;
  }
  
  List<Picture> getPictures() {
    return pictures;
  }
  
  final class PictureReader implements Runnable {
    private final File file;
    
    private final User user;
    
    public PictureReader(final File file, final User user) {
      this.file = file;
      this.user = user;
    }
    
    @Override
    public void run() {
      final String fileName = file.getAbsolutePath();
      try {
        final Metadata metadata = ImageMetadataReader.readMetadata(file);
        final ExifThumbnailDirectory dir = metadata.getDirectory(ExifThumbnailDirectory.class);
        final byte[] thumbnail;
        if (dir.hasThumbnailData()) {
          thumbnail = dir.getThumbnailData();
        } else {
          thumbnail = new byte[0];
        }
        final Picture picture = new Picture(file, fileName, defaultTemplate, metadata, thumbnail);
        picture.renderTemplate(user);
        pictures.add(picture);
      } catch (ImageProcessingException e) {
        LOGGER.warn("Can't read metadata from {}", fileName, e);
      } catch (IOException e) {
        LOGGER.warn("Can't read file {}", fileName, e);
      }
      latch.countDown();
    }
  }
}
