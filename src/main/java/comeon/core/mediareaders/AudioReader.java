package comeon.core.mediareaders;

import comeon.core.MediaUploadBatch;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class AudioReader extends AbstractMediaReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioReader.class);

    private static final EnumMap<FieldKey, String> FIELD_KEY_NAMES = new EnumMap<>(FieldKey.class);

    private static final LazyDynaClass TAG_DYNA_CLASS;

    static {
        final DynaProperty[] properties = Arrays.stream(FieldKey.values())
                .filter(Predicate.isEqual(FieldKey.COVER_ART).negate())
                .map(AudioReader::toName)
                .map(name -> new DynaProperty(name, String.class)).toArray(DynaProperty[]::new);

        TAG_DYNA_CLASS = new LazyDynaClass(Tag.class.getName(), null, properties);
        TAG_DYNA_CLASS.setReturnNull(true);

        FIELD_KEY_NAMES.putAll(Arrays.stream(FieldKey.values())
                .filter(Predicate.isEqual(FieldKey.COVER_ART).negate())
                .collect(Collectors.toMap(key -> key, AudioReader::toName)));

    }

    private static String toName(final FieldKey key) {
        return WordUtils.uncapitalize(WordUtils.capitalizeFully(key.name().replace('_', ' ')).replace(" ", ""));
    }

    public AudioReader(final File file, final User user) {
        super(file, user);
    }

    @Override
    protected Media buildMedia(final MediaUploadBatch context) throws MediaReaderException, IOException {
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

            return new Media(getFile(), fileName, context.getDefaultTemplate(), metadata, thumbnail);
        } catch (final CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            throw new MediaReaderException(e);
        }
    }

    private DynaBean copyTags(final Tag tag) {
        final LazyDynaBean bean = new LazyDynaBean(TAG_DYNA_CLASS);
        FIELD_KEY_NAMES.entrySet().forEach(e -> bean.set(e.getValue(), tag.getFirst(e.getKey())));
        return bean;
    }

    private DynaBean copyHeaders(final AudioHeader header) {
        final WrapDynaClass clazz = WrapDynaClass.createDynaClass(header.getClass());
        final LazyDynaBean bean = new LazyDynaBean();
        Arrays.stream(clazz.getDynaProperties())
                .map(DynaProperty::getName)
                .filter(Predicate.isEqual("class").negate())
                .forEach(name -> {
                    try {
                        bean.set(name, String.valueOf(PropertyUtils.getProperty(header, name)));
                    } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        LOGGER.warn("Could not read property {} from {}", name, header);
                    }
                });
        return bean;
    }
}
