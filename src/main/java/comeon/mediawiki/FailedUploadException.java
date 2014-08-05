package comeon.mediawiki;

import java.io.IOException;

public final class FailedUploadException extends MediaWikiException {
  private static final long serialVersionUID = 1L;

  private static final String MESSAGE_FORMAT = "%1$s: %2$s";

  public FailedUploadException(final IOException cause) {
    super(cause);
  }
  
  public FailedUploadException(final String code, final String info) {
    super(String.format(MESSAGE_FORMAT, code, info));
  }
}
