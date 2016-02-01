package comeon.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.model.Media;
import comeon.model.Template;
import comeon.model.User;
import comeon.model.processors.PreProcessor;

public final class MediaUploadBatch {
  static final Logger LOGGER = LoggerFactory.getLogger(MediaUploadBatch.class);
  
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
      this.execute(selectMediaReader(file, user));
    }

    try {
      latch.await();
    } catch (final InterruptedException e) {
      Thread.interrupted();
    }

    return this;
  }
  
  private Runnable selectMediaReader(final File file, final User user) {
    if (isPicture(file)) {
      return new PictureReader(this, file, user);
    } else if (isAudio(file)) {
      return new AudioReader(this, file, user);
    } else {
      throw new Error("No media reader for this file: " + file.getName());
    }
  }
  
  private boolean isPicture(final File file) {
    return file.getName().matches("(?i).*\\.jpe?g$");
  }
  
  private boolean isAudio(final File file) {
    return file.getName().matches("(?i).*\\.flac$");
  }
  
  public List<Media> getMedia() {
    return medias;
  }
  
  void add(final Media media) {
    this.medias.add(media);
  }
  
  void decreaseLatch() {
    this.latch.countDown();
  }
  
  void execute(final Runnable r) {
    pool.execute(r);
  }
  
  Template getDefaultTemplate() {
    return defaultTemplate;
  }

  Object getMediaMetadata(Media media, Map<String, Object> mediaMetadata) {
    return externalMetadataSource.getMediaMetadata(media, mediaMetadata);
  }
  
  Set<PreProcessor> filterPreProcessors(final Predicate<PreProcessor> predicate) {
    return Sets.filter(preProcessors, predicate);
  }
}
