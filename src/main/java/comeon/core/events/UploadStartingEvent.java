package comeon.core.events;

import comeon.model.Media;

import java.util.List;

public final class UploadStartingEvent {

    private final List<Media> media;

    public UploadStartingEvent(List<Media> media) {
        this.media = media;
    }

    public List<Media> getMedia() {
        return media;
    }
}
