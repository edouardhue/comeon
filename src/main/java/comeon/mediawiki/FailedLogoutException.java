package comeon.mediawiki;

import java.io.IOException;

public final class FailedLogoutException extends MediaWikiException {

  private static final long serialVersionUID = 1L;

  public FailedLogoutException(final IOException cause) {
    super(cause);
  }
}
