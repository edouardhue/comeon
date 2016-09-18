package comeon.core;

import comeon.mediawiki.ImageInfo;
import comeon.model.Media;

public class UploadReport {
    private final Media media;

    private final ImageInfo info;

    private final Throwable cause;

    public UploadReport(final Media media, final ImageInfo info) {
        this(media, info, null);
    }

    public UploadReport(final Media media, final Throwable cause) {
        this(media, null, cause);
    }

    private UploadReport(Media media, ImageInfo info, Throwable cause) {
        this.media = media;
        this.info = info;
        this.cause = cause;
    }

    public Media getMedia() {
        return media;
    }

    public ImageInfo getInfo() {
        return info;
    }

    public Throwable getCause() {
        return cause;
    }
}
