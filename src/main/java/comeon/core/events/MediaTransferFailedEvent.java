package comeon.core.events;

import comeon.model.Media;

public final class MediaTransferFailedEvent extends AbstractMediaEvent {

    private final Throwable cause;

    public MediaTransferFailedEvent(final Media media, final Throwable cause) {
        super(media);
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }

}
