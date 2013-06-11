package comeon.commons;

import java.io.IOException;

public final class FailedLogoutException extends CommonsException {

  private static final long serialVersionUID = 1L;

  public FailedLogoutException(final IOException cause) {
    super(cause);
  }
}
