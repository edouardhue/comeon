package comeon.ui.actions;

import comeon.model.Picture;

public final class PictureRemovedEvent {
  private final Picture picture;

  public PictureRemovedEvent(final Picture picture) {
    this.picture = picture;
  }
  
  public Picture getPicture() {
    return picture;
  }
}
