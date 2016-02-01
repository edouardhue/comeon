package comeon.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.LazyDynaClass;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.TagDescriptor;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import com.google.common.base.Predicate;

import comeon.model.Media;
import comeon.model.User;
import comeon.model.processors.PreProcessor;

public final class PictureReader extends AbstractMediaReader {
  
  private static final String NON_WORD_CHARS = "[^\\w]";
  
  public PictureReader(final MediaUploadBatch mediaUploadBatch, final File file, final User user) {
    super(mediaUploadBatch, file, user);
  }

  protected final Media buildMedia() throws MediaReaderException, IOException {
    final String fileName = getFile().getAbsolutePath();
    try {
      final Metadata rawMetadata = ImageMetadataReader.readMetadata(getFile());
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
      return new Media(getFile(), fileName, getMediaUploadBatch().getDefaultTemplate(), metadata, thumbnail);
    } catch (final ImageProcessingException e) {
      throw new MediaReaderException(e);
    }
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
    final Set<PreProcessor> preProcessors = getMediaUploadBatch().filterPreProcessors(new DirectoryPreProcessor(directory.getClass()));
    for (final PreProcessor preProcessor : preProcessors) {
      preProcessor.process(directory, metadata);
    }
  }
  
  final static class DirectoryPreProcessor implements Predicate<PreProcessor> {
    private final Class<? extends Directory> clazz;
    
    public DirectoryPreProcessor(final Class<? extends Directory> clazz) {
      this.clazz = clazz;
    }
    
    @Override
    public boolean apply(final PreProcessor processor) {
      return clazz.isAssignableFrom(processor.getSupportedClass());
    }
  }

}
