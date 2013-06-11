package comeon.commons;

import java.io.IOException;

public final class FailedUploadException extends CommonsException {

  private static final long serialVersionUID = 1L;

  public FailedUploadException(final IOException cause) {
    super(cause);
  }
}
