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
import comeon.model.Media;
import comeon.model.Template;
import comeon.model.User;
import comeon.model.processors.PreProcessor;

public final class MediaUploadBatch {
  private static final Logger LOGGER = LoggerFactory.getLogger(MediaUploadBatch.class);
  
  private final File[] files;

  private final Template defaultTemplate;

  private final List<Media> medias;

  private final ExecutorService pool;

  private final CountDownLatch latch;
  
  private final Set<PreProcessor> preProcessors;
  
  private final ExternalMetadataSource<?> externalMetadataSource;

  MediaUploadBatch(final File[] files, final Template defautTemplate, final ExecutorService pool, final Set<PreProcessor> preProcessors, final ExternalMetadataSource<?> externalMetadataSource) {
    this.files = files;
    this.defaultTemplate = defautTemplate;
    this.pool = pool;
    this.medias = Collections.synchronizedList(new ArrayList<Media>(files.length));
    this.latch = new CountDownLatch(files.length);
    this.preProcessors = preProcessors;
    this.externalMetadataSource = externalMetadataSource;
  }

  public MediaUploadBatch readFiles(final User user) {
    for (final File file : files) {
      pool.execute(new MediaReader(file, user));
    }

    try {
      latch.await();
    } catch (final InterruptedException e) {
      Thread.interrupted();
    }

    return this;
  }

  public List<Media> getMedia() {
    return medias;
  }

  final class MediaReader implements Runnable {
    private static final String NON_WORD_CHARS = "[^\\w]";

    private final File file;

    private final User user;

    MediaReader(final File file, final User user) {
      this.file = file;
      this.user = user;
    }

    @Override
    public void run() {
      final String fileName = file.getAbsolutePath();
      try {
        final Media media = buildMedia();
        media.renderTemplate(user);
        media.addPropertyChangeListener(new PropertyChangeListener() {
          @Override
          public void propertyChange(final PropertyChangeEvent evt) {
            if ("templateText".equals(evt.getPropertyName())) {
              pool.submit(new Runnable() {
                @Override
                public void run() {
                  media.renderTemplate(user);
                }
              });
            }
          }
        });
        medias.add(media);
      } catch (final ImageProcessingException e) {
        LOGGER.warn("Can't read metadata from {}", fileName, e);
      } catch (final IOException e) {
        LOGGER.warn("Can't read file {}", fileName, e);
      } finally {
        latch.countDown();
      }
    }
    
    Media buildMedia() throws ImageProcessingException, IOException {
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
      final Media media = new Media(file, fileName, defaultTemplate, metadata, thumbnail);
      metadata.put(Core.EXTERNAL_METADATA_KEY, externalMetadataSource.getMediaMetadata(media, metadata));
      return media;
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
