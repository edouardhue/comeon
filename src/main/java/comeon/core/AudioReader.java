package comeon.core;

import comeon.model.Media;
import comeon.model.User;
import org.apache.commons.beanutils.*;
import org.apache.commons.lang.WordUtils;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class AudioReader extends AbstractMediaReader {

    private static final EnumMap<FieldKey, String> FIELD_KEY_NAMES = new EnumMap<>(FieldKey.class);

    private static final LazyDynaClass TAG_DYNA_CLASS;

    static {
        final List<DynaProperty> properties = new ArrayList<>(FieldKey.values().length);

        for (final FieldKey key : FieldKey.values()) {
            if (!FieldKey.COVER_ART.equals(key)) {
                final String name = toName(key);
                FIELD_KEY_NAMES.put(key, name);
                properties.add(new DynaProperty(name, String.class));
            }
        }

        TAG_DYNA_CLASS = new LazyDynaClass(Tag.class.getName(), null, properties.toArray(new DynaProperty[properties.size()]));
        TAG_DYNA_CLASS.setReturnNull(true);
    }

    private static final String toName(final FieldKey key) {
        return WordUtils.uncapitalize(WordUtils.capitalizeFully(key.name().replace('_', ' ')).replace(" ", ""));
    }

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
        final LazyDynaBean bean = new LazyDynaBean(TAG_DYNA_CLASS);
        for (final Map.Entry<FieldKey, String> entry : FIELD_KEY_NAMES.entrySet()) {
            bean.set(entry.getValue(), tag.getFirst(entry.getKey()));
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
