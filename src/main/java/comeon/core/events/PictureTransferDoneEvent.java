package comeon.core.events;

import comeon.model.Picture;

public final class PictureTransferDoneEvent extends AbstractPictureEvent {

  public PictureTransferDoneEvent(final Picture picture) {
    super(picture);
  }
  
}
