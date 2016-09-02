package comeon.ui.actions;

import comeon.model.Media;

public final class MediaRemovedEvent {
    private final Media media;

    public MediaRemovedEvent(final Media media) {
        this.media = media;
    }

    public Media getMedia() {
        return media;
    }
}
