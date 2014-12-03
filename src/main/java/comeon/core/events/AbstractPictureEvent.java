package comeon.core.events;

import comeon.model.Picture;

abstract class AbstractPictureEvent {

  private final Picture picture;

  protected AbstractPictureEvent(final Picture picture) {
    this.picture = picture;
  }

  public final Picture getPicture() {
    return picture;
  }

}