package comeon.core.events;

import comeon.model.Picture;

public final class PictureTransferFailedEvent extends AbstractPictureEvent {

  private final Exception cause;
  
  public PictureTransferFailedEvent(final Picture picture, final Exception cause) {
    super(picture);
    this.cause = cause;
  }
  
  public Exception getCause() {
    return cause;
  }

}
