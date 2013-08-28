package comeon.mediawiki;

import java.io.IOException;

public final class FailedUploadException extends MediaWikiException {

  private static final long serialVersionUID = 1L;

  public FailedUploadException(final IOException cause) {
    super(cause);
  }
}
