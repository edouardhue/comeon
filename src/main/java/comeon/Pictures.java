package comeon;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.LazyDynaClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.TagDescriptor;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.iptc.IptcDirectory;
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
        final Metadata rawMetadata = ImageMetadataReader.readMetadata(file);
        final ExifThumbnailDirectory thumbnailDirectory = rawMetadata.getDirectory(ExifThumbnailDirectory.class);
        final byte[] thumbnail;
        if (thumbnailDirectory.hasThumbnailData()) {
          thumbnail = thumbnailDirectory.getThumbnailData();
        } else {
          thumbnail = new byte[0];
        }
        final Map<String, Object> metadata = new HashMap<>(rawMetadata.getDirectoryCount());
        for (final Directory directory : rawMetadata.getDirectories()) {
          final TagDescriptor<?> descriptor = MetadataHelper.getDescriptor(directory);
          final List<DynaProperty> properties = new LinkedList<>();
          for (final Tag tag : directory.getTags()) {
            final DynaProperty property = new DynaProperty(tag.getTagName().replaceAll("[^\\w]", ""), String.class);
            properties.add(property);
          }
          final LazyDynaClass directoryClass = new LazyDynaClass(directory.getName(), null, properties.toArray(new DynaProperty[properties.size()]));
          directoryClass.setReturnNull(true);
          final DynaBean directoryMetadata = new LazyDynaBean(directoryClass);
          for (final Tag tag : directory.getTags()) {
            directoryMetadata.set(tag.getTagName().replaceAll("[^\\w]", ""), descriptor.getDescription(tag.getTagType()));
          }
          metadata.put(directory.getName(), directoryMetadata);
          if (IptcDirectory.class.equals(directory.getClass())) {
            final IptcDirectory iptcDirectory = (IptcDirectory) directory;
            final String[] keywords = iptcDirectory.getStringArray(IptcDirectory.TAG_KEYWORDS);
            metadata.put("keywords", keywords);
            final String iptcDate = iptcDirectory.getString(IptcDirectory.TAG_DIGITAL_DATE_CREATED);
            final String iptcTime = iptcDirectory.getString(IptcDirectory.TAG_DIGITAL_TIME_CREATED);
            final SimpleDateFormat inFormat = new SimpleDateFormat("HHmmss:yyyyMMdd", Locale.ENGLISH);
            final SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            try {
              final Date pictureDate = inFormat.parse(iptcTime + ":" + iptcDate);
              metadata.put("date", outFormat.format(pictureDate));
            } catch (final ParseException e) {
              LOGGER.warn("Can't handle date", e);
            }
          }
          if (GpsDirectory.class.equals(directory.getClass())) {
            final GeoLocation geolocation = ((GpsDirectory) directory).getGeoLocation();
            metadata.put("geolocation", geolocation);
          }
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
