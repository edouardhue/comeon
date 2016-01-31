package comeon.core.events;

import comeon.core.ProgressListenerAdapter;
import comeon.model.Media;

public final class MediaTransferStartingEvent extends AbstractMediaEvent {

  private final ProgressListenerAdapter progressListener;
  
  public MediaTransferStartingEvent(final Media media, final ProgressListenerAdapter progressListener) {
    super(media);
    this.progressListener = progressListener;
  }
  
  public ProgressListenerAdapter getProgressListener() {
    return progressListener;
  }
}
