package comeon.core;

import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.core.mediareaders.AudioReader;
import comeon.core.mediareaders.MediaReader;
import comeon.core.mediareaders.PictureReader;
import comeon.model.Media;
import comeon.model.Template;
import comeon.model.User;
import comeon.model.processors.PreProcessor;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public final class MediaUploadBatch {

    private final File[] files;

    private final Template template;

    private final Set<Media> media;

    private final Set<PreProcessor> preProcessors;

    private final ExternalMetadataSource<?> externalMetadataSource;

    public MediaUploadBatch(final File[] files, final Template template, final Set<PreProcessor> preProcessors, final ExternalMetadataSource<?> externalMetadataSource) {
        this.files = files;
        this.template = template;
        this.media = new HashSet<>();
        this.preProcessors = preProcessors;
        this.externalMetadataSource = externalMetadataSource;
    }

    public MediaUploadBatch readFiles(final User user) {
        // FIXME Use a parallel stream. Requires custom ForkJoinPool thread factory.
        media.addAll(Arrays.stream(files)
                .map(f -> selectMediaReader(f, user))
                .map(r -> r.readMedia(this))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));
        return this;
    }

    private MediaReader selectMediaReader(final File file, final User user) {
        if (isPicture(file)) {
            return new PictureReader(file, user);
        } else if (isAudio(file)) {
            return new AudioReader(file, user);
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

    public Set<Media> getMedia() {
        return Collections.unmodifiableSet(media);
    }

    public Template getTemplate() {
        return template;
    }

    public Object getExternalMetadata(Media media, Map<String, Object> mediaMetadata) {
        return externalMetadataSource.getMediaMetadata(media, mediaMetadata);
    }

    public Set<PreProcessor> getPreProcessors() {
        return Collections.unmodifiableSet(preProcessors);
    }

    public ExternalMetadataSource<?> getExternalMetadataSource() {
        return externalMetadataSource;
    }
}
