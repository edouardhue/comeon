package comeon.ui.actions;

import java.util.List;

import comeon.model.Media;

public final class MediaAddedEvent {
  private final List<Media> medias;

  public MediaAddedEvent(final List<Media> medias) {
    this.medias = medias;
  }
  
  public List<Media> getMedia() {
    return medias;
  }
  
}
