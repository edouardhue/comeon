package comeon.core.events;

import comeon.core.MediaUploadBatch;
import comeon.model.Media;

import java.util.List;

public final class MediaAddedEvent {
    private final MediaUploadBatch batch;

    public MediaAddedEvent(final MediaUploadBatch batch) {
        this.batch = batch;
    }

    public MediaUploadBatch getBatch() {
        return batch;
    }
}
