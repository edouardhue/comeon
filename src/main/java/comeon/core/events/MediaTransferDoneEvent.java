package comeon.core.events;

import comeon.model.Media;

public final class MediaTransferDoneEvent extends AbstractMediaEvent {

  public MediaTransferDoneEvent(final Media media) {
    super(media);
  }
  
}
