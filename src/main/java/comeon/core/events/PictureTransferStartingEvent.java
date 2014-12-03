package comeon.core.events;

import comeon.core.ProgressListenerAdapter;
import comeon.model.Picture;

public final class PictureTransferStartingEvent extends AbstractPictureEvent {

  private final ProgressListenerAdapter progressListener;
  
  public PictureTransferStartingEvent(final Picture picture, final ProgressListenerAdapter progressListener) {
    super(picture);
    this.progressListener = progressListener;
  }
  
  public ProgressListenerAdapter getProgressListener() {
    return progressListener;
  }
}
