package comeon.core.mediareaders;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.TagDescriptor;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import comeon.core.MediaUploadBatch;
import comeon.model.Media;
import comeon.model.User;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.LazyDynaClass;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class PictureReader extends AbstractMediaReader {

    private static final String NON_WORD_CHARS = "[^\\w]";

    public PictureReader(final File file, final User user) {
        super(file, user);
    }

    protected final Media buildMedia(final MediaUploadBatch context) throws MediaReaderException, IOException {
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
                preProcess(context, directory, metadata);
            }
            return new Media(getFile(), fileName, context.getTemplate(), metadata, thumbnail);
        } catch (final ImageProcessingException e) {
            throw new MediaReaderException(e);
        }
    }

    private void copy(final Directory directory, final Map<String, Object> metadata) {
        final TagDescriptor<?> descriptor = MetadataHelper.getDescriptor(directory);

        final LazyDynaClass directoryClass = new LazyDynaClass(directory.getName(), null, directory.getTags()
                .parallelStream()
                .map(t -> new DynaProperty(t.getTagName().replaceAll(NON_WORD_CHARS, ""), String.class))
                .toArray(DynaProperty[]::new));
        directoryClass.setReturnNull(true);

        final DynaBean directoryMetadata = new LazyDynaBean(directoryClass);
        directory.getTags().stream().forEach(t -> directoryMetadata.set(
                t.getTagName().replaceAll(NON_WORD_CHARS, ""),
                descriptor.getDescription(t.getTagType())
        ));
        metadata.put(directory.getName().replaceAll(NON_WORD_CHARS, ""), directoryMetadata);
    }

    private void preProcess(final MediaUploadBatch context, final Directory directory, final Map<String, Object> metadata) {
        context.getPreProcessors().stream()
                .filter(p -> directory.getClass().isAssignableFrom(p.getSupportedClass()))
                .forEach(p -> p.process(directory, metadata));
    }

}
