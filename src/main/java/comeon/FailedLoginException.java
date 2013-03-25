package comeon;

import java.io.IOException;

public final class FailedLoginException extends CommonsException {
  private static final long serialVersionUID = 1L;

  public FailedLoginException(final IOException cause) {
    super(cause);
  }
}
