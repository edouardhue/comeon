package comeon.core.events;

import java.util.List;

import comeon.model.Picture;

public final class UploadStartingEvent {

  private final List<Picture> pictures;

  public UploadStartingEvent(List<Picture> pictures) {
    this.pictures = pictures;
  }

  public List<Picture> getPictures() {
    return pictures;
  }
}
