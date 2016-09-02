package comeon.core.events;

import comeon.model.Media;

abstract class AbstractMediaEvent {

    private final Media media;

    protected AbstractMediaEvent(final Media media) {
        this.media = media;
    }

    public final Media getMedia() {
        return media;
    }

}