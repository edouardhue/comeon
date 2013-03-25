package comeon;

class CommonsException extends Exception {

  public CommonsException() {
    super();
  }

  public CommonsException(final String message, final Throwable cause, final boolean enableSuppression,
      final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public CommonsException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public CommonsException(final String message) {
    super(message);
  }

  public CommonsException(final Throwable cause) {
    super(cause);
  }

  private static final long serialVersionUID = 1L;

}
