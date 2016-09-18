package comeon.mediawiki;

public final class ImageInfo {

    private final String canonicalTitle;

    private final String url;

    private final String descriptionUrl;

    public ImageInfo(final String canonicalTitle, final String url, final String descriptionUrl) {
        this.canonicalTitle = canonicalTitle;
        this.url = url;
        this.descriptionUrl = descriptionUrl;
    }

    public String getCanonicalTitle() {
        return canonicalTitle;
    }

    public String getUrl() {
        return url;
    }

    public String getDescriptionUrl() {
        return descriptionUrl;
    }
}
