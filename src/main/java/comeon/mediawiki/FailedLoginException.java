package comeon.mediawiki;

public final class FailedLoginException extends MediaWikiException {
  private static final long serialVersionUID = 1L;

  public FailedLoginException(final Exception cause) {
    super(cause);
  }
}
