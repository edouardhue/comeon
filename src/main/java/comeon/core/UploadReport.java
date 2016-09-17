package comeon.core;

import comeon.model.Media;

import java.util.Optional;

public class UploadReport {
    private final Media media;

    private final Throwable cause;

    public UploadReport(final Media media) {
        this(media, null);
    }

    public UploadReport(final Media media, final Throwable cause) {
        this.media = media;
        this.cause = cause;
    }

    public Media getMedia() {
        return media;
    }

    public Throwable getCause() {
        return cause;
    }
}
