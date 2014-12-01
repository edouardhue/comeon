package comeon.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.TagDescriptor;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.model.Picture;
import comeon.model.Template;
import comeon.model.User;
import comeon.model.processors.PreProcessor;

public final class PicturesBatch {
  private static final Logger LOGGER = LoggerFactory.getLogger(PicturesBatch.class);
  
  private final File[] files;

  private final Template defaultTemplate;

  private final List<Picture> pictures;

  private final ExecutorService pool;

  private final CountDownLatch latch;
  
  private final Set<PreProcessor> preProcessors;
  
  private final ExternalMetadataSource<?> externalMetadataSource;

  PicturesBatch(final File[] files, final Template defautTemplate, final ExecutorService pool, final Set<PreProcessor> preProcessors, final ExternalMetadataSource<?> externalMetadataSource) {
    this.files = files;
    this.defaultTemplate = defautTemplate;
    this.pool = pool;
    this.pictures = Collections.synchronizedList(new ArrayList<Picture>(files.length));
    this.latch = new CountDownLatch(files.length);
    this.preProcessors = preProcessors;
    this.externalMetadataSource = externalMetadataSource;
  }

  public PicturesBatch readFiles(final User user) {
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

  public List<Picture> getPictures() {
    return pictures;
  }

  final class PictureReader implements Runnable {
    private static final String NON_WORD_CHARS = "[^\\w]";

    private final File file;

    private final User user;

    PictureReader(final File file, final User user) {
      this.file = file;
      this.user = user;
    }

    @Override
    public void run() {
      final String fileName = file.getAbsolutePath();
      try {
        final Picture picture = buildPicture();
        picture.renderTemplate(user);
        picture.addPropertyChangeListener(new PropertyChangeListener() {
          @Override
          public void propertyChange(final PropertyChangeEvent evt) {
            if ("templateText".equals(evt.getPropertyName())) {
              pool.submit(new Runnable() {
                @Override
                public void run() {
                  picture.renderTemplate(user);
                }
              });
            }
          }
        });
        pictures.add(picture);
      } catch (final ImageProcessingException e) {
        LOGGER.warn("Can't read metadata from {}", fileName, e);
      } catch (final IOException e) {
        LOGGER.warn("Can't read file {}", fileName, e);
      } finally {
        latch.countDown();
      }
    }
    
    Picture buildPicture() throws ImageProcessingException, IOException {
      final String fileName = file.getAbsolutePath();
      final Metadata rawMetadata = ImageMetadataReader.readMetadata(file);
      final ExifThumbnailDirectory thumbnailDirectory = rawMetadata.getDirectory(ExifThumbnailDirectory.class);
      final byte[] thumbnail;
      if (thumbnailDirectory != null && thumbnailDirectory.hasThumbnailData()) {
        thumbnail = thumbnailDirectory.getThumbnailData();
      } else {
        thumbnail = new byte[0];
      }
      final Map<String, Object> metadata = new HashMap<>(rawMetadata.getDirectoryCount());
      for (final Directory directory : rawMetadata.getDirectories()) {
        copy(directory, metadata);
        preProcess(directory, metadata);
      }
      final Picture picture = new Picture(file, fileName, defaultTemplate, metadata, thumbnail);
      metadata.put(Core.EXTERNAL_METADATA_KEY, externalMetadataSource.getPictureMetadata(picture, metadata));
      return picture;
    }

    private void copy(final Directory directory, final Map<String, Object> metadata) {
      final TagDescriptor<?> descriptor = MetadataHelper.getDescriptor(directory);
      final List<DynaProperty> properties = new LinkedList<>();
      for (final Tag tag : directory.getTags()) {
        final DynaProperty property = new DynaProperty(tag.getTagName().replaceAll(NON_WORD_CHARS, ""), String.class);
        properties.add(property);
      }
      final LazyDynaClass directoryClass = new LazyDynaClass(directory.getName(), null,
          properties.toArray(new DynaProperty[properties.size()]));
      directoryClass.setReturnNull(true);
      final DynaBean directoryMetadata = new LazyDynaBean(directoryClass);
      for (final Tag tag : directory.getTags()) {
        directoryMetadata.set(tag.getTagName().replaceAll(NON_WORD_CHARS, ""),
            descriptor.getDescription(tag.getTagType()));
      }
      metadata.put(directory.getName().replaceAll(NON_WORD_CHARS, ""), directoryMetadata);
    }

    private void preProcess(final Directory directory, final Map<String, Object> metadata) {
      final Set<PreProcessor> preProcessors = filterPreProcessors(directory.getClass());
      for (final PreProcessor preProcessor : preProcessors) {
        preProcessor.process(directory, metadata);
      }
    }
    
    private Set<PreProcessor> filterPreProcessors(final Class<? extends Directory> clazz) {
      final Predicate<PreProcessor> predicate = new Predicate<PreProcessor>() {
        @Override
        public boolean apply(final PreProcessor processor) {
          return clazz.isAssignableFrom(processor.getSupportedClass());
        }
      };
      return Sets.filter(preProcessors, predicate);
    }
  }
}
