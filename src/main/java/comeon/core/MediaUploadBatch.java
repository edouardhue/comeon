package comeon.core;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.model.Media;
import comeon.model.Template;
import comeon.model.User;
import comeon.model.processors.PreProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public final class MediaUploadBatch {
    static final Logger LOGGER = LoggerFactory.getLogger(MediaUploadBatch.class);

    private final File[] files;

    private final Template defaultTemplate;

    private final List<Media> medias;

    private final Set<PreProcessor> preProcessors;

    private final ExternalMetadataSource<?> externalMetadataSource;

    MediaUploadBatch(final File[] files, final Template defautTemplate, final Set<PreProcessor> preProcessors, final ExternalMetadataSource<?> externalMetadataSource) {
        this.files = files;
        this.defaultTemplate = defautTemplate;
        this.medias = Collections.synchronizedList(new ArrayList<Media>(files.length));
        this.preProcessors = preProcessors;
        this.externalMetadataSource = externalMetadataSource;
    }

    public MediaUploadBatch readFiles(final User user) {
        Arrays.stream(files).parallel().map(f -> selectMediaReader(f, user)).forEach(r -> r.run());
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
        return Arrays.stream(Core.PICTURE_EXTENSIONS).anyMatch(e -> file.getName().toLowerCase(Locale.ENGLISH).endsWith(e));
    }

    private boolean isAudio(final File file) {
        return Arrays.stream(Core.AUDIO_EXTENSIONS).anyMatch(e -> file.getName().toLowerCase(Locale.ENGLISH).endsWith(e));
    }

    public List<Media> getMedia() {
        return medias;
    }

    void add(final Media media) {
        this.medias.add(media);
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
