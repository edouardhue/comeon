package comeon.ui.actions;

import comeon.model.Media;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MediaRemovedEvent {
    private final List<Media> media;

    public MediaRemovedEvent(final Media media) {
        this.media = Collections.singletonList(media);
    }

    public MediaRemovedEvent(final List<Media> media) {
        this.media = new ArrayList<>(media);
    }

    public List<Media> getMedia() {
        return Collections.unmodifiableList(media);
    }
}
