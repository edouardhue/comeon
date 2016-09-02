package comeon.mediawiki;

class MediaWikiException extends Exception {

    public MediaWikiException() {
        super();
    }

    public MediaWikiException(final String message, final Throwable cause, final boolean enableSuppression,
                              final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MediaWikiException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MediaWikiException(final String message) {
        super(message);
    }

    public MediaWikiException(final Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = 1L;

}
