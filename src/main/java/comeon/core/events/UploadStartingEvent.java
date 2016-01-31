package comeon.core.events;

import java.util.List;

import comeon.model.Media;

public final class UploadStartingEvent {

  private final List<Media> media;

  public UploadStartingEvent(List<Media> media) {
    this.media = media;
  }

  public List<Media> getMedia() {
    return media;
  }
}
