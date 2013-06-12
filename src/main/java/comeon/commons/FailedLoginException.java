package comeon.commons;

public final class FailedLoginException extends CommonsException {
  private static final long serialVersionUID = 1L;

  public FailedLoginException(final Exception cause) {
    super(cause);
  }
}
