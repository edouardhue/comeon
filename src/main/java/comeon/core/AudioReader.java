package comeon.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.LazyDynaClass;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.WrapDynaClass;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;

import comeon.model.Media;
import comeon.model.User;

public final class AudioReader extends AbstractMediaReader {

  public AudioReader(final MediaUploadBatch mediaUploadBatch, final File file, final User user) {
    super(mediaUploadBatch, file, user);
  }

  @Override
  protected Media buildMedia() throws MediaReaderException, IOException {
    final String fileName = getFile().getAbsolutePath();
    try {
      final AudioFile audioFile = AudioFileIO.read(getFile());
      final Tag tag = audioFile.getTag();
      final AudioHeader header = audioFile.getAudioHeader();

      final byte[] thumbnail;
      final List<Artwork> artwork = tag.getArtworkList();
      if (artwork.isEmpty()) {
        thumbnail = new byte[0];
      } else {
        thumbnail = artwork.get(0).getBinaryData();
      }

      final Map<String, Object> metadata = new HashMap<>(2);
      metadata.put("Tags", copyTags(tag));
      metadata.put("Headers", copyHeaders(header));

      return new Media(getFile(), fileName, getMediaUploadBatch().getDefaultTemplate(), metadata, thumbnail);
    } catch (final CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
      throw new MediaReaderException(e);
    }
  }
  
  private DynaBean copyTags(final Tag tag) {
    final List<DynaProperty> properties = new ArrayList<>(FieldKey.values().length);
    for (final FieldKey key : FieldKey.values()) {
      if (!FieldKey.COVER_ART.equals(key)) {
        properties.add(new DynaProperty(key.name(), String.class));
      }
    }
    final LazyDynaClass clazz = new LazyDynaClass(tag.getClass().getName(), null, properties.toArray(new DynaProperty[properties.size()]));
    clazz.setReturnNull(true);
    final LazyDynaBean bean = new LazyDynaBean(clazz);
    for (final DynaProperty property : properties) {
      bean.set(property.getName(), tag.getFirst(property.getName()));
    }
    return bean;
  }

  private DynaBean copyHeaders(final AudioHeader header) {
    final WrapDynaClass clazz = WrapDynaClass.createDynaClass(header.getClass());
    final LazyDynaBean bean = new LazyDynaBean();
    for (final DynaProperty property : clazz.getDynaProperties()) {
      if (!"class".equals(property.getName())) {
        try {
          bean.set(property.getName(), PropertyUtils.getProperty(header, property.getName()));
        } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
          // Woops
        }
      }
    }
    return bean;
  }
}
