package comeon.ui.actions;

import comeon.model.Media;

import java.util.List;

public final class MediaAddedEvent {
    private final List<Media> medias;

    public MediaAddedEvent(final List<Media> medias) {
        this.medias = medias;
    }

    public List<Media> getMedia() {
        return medias;
    }

}
