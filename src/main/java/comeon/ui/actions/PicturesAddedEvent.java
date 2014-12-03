package comeon.ui.actions;

import java.util.List;

import comeon.model.Picture;

public final class PicturesAddedEvent {
  private final List<Picture> pictures;

  public PicturesAddedEvent(final List<Picture> pictures) {
    this.pictures = pictures;
  }
  
  public List<Picture> getPictures() {
    return pictures;
  }
  
}
