package comeon.core;

public final class MediaReaderException extends Exception {

  private static final long serialVersionUID = 1L;

  public MediaReaderException() {
  }

  public MediaReaderException(final String message) {
    super(message);
  }

  public MediaReaderException(final Throwable cause) {
    super(cause);
  }

  public MediaReaderException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public MediaReaderException(final String message, final Throwable cause, final boolean enableSuppression,
      final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
