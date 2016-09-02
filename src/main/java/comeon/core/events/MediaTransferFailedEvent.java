package comeon.core.events;

import comeon.model.Media;

public final class MediaTransferFailedEvent extends AbstractMediaEvent {

    private final Exception cause;

    public MediaTransferFailedEvent(final Media media, final Exception cause) {
        super(media);
        this.cause = cause;
    }

    public Exception getCause() {
        return cause;
    }

}
