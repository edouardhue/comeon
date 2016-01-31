package comeon.mediawiki;

import in.yuvi.http.fluent.ProgressListener;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

import org.mediawiki.api.ApiResult;
import org.mediawiki.api.MWApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import comeon.model.Media;
import comeon.model.User;
import comeon.model.Wiki;
import comeon.ui.UI;

public final class MediaWikiImpl implements MediaWiki {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(MediaWikiImpl.class);

  private final MWApi api;

  private final Wiki wiki;
  
  MediaWikiImpl(final Wiki wiki, final MWApi api) {
    this.wiki = wiki;
    this.api = api;
  }

  /*
   * (non-Javadoc)
   * 
   * @see comeon.commons.Commons#login()
   */
  @Override
  public void login() throws NotLoggedInException, FailedLoginException {
    try {
      LOGGER.debug("Logging in");
      final User user = wiki.getUser();
      final String result = this.api.login(user.getLogin(), user.getPassword());
      if (!this.api.isLoggedIn) {
        throw new NotLoggedInException(result);
      }
    } catch (final IOException e) {
      throw new FailedLoginException(e);
    }
  }

  @Override
  public boolean isLoggedIn() {
    return api.isLoggedIn;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see comeon.commons.Commons#upload(comeon.model.Media,
   * in.yuvi.http.fluent.ProgressListener)
   */
  @Override
  public void upload(final Media media, final ProgressListener listener) throws NotLoggedInException,
      FailedLoginException, FailedUploadException, IOException {
    synchronized (this) {
      if (!this.api.isLoggedIn) {
        LOGGER.debug("Not logged in");
        this.login();
      }
    }
    final InputStream stream = Files.asByteSource(media.getFile()).openBufferedStream();
    try {
      LOGGER.debug("Uploading");
      final ApiResult result = this.api.upload(media.getFile().getName(), stream, media.getFile().length(), media.getRenderedTemplate(),
          MessageFormat.format(UI.BUNDLE.getString("upload.comment"), UI.BUNDLE.getString("comeon")), true, listener);
      final ApiResult error = result.getNode("/api/error");
      if (error.getDocument() != null) {
        final String code = error.getString("@code");
        final String info = error.getString("@info");
        throw new FailedUploadException(code, info);
      }
      final List<ApiResult> warnings = result.getNodes("/api/warning");
      if (warnings != null && !warnings.isEmpty()) {
        for (final ApiResult warning : warnings) {
          final String code = warning.getString("@code");
          final String info = warning.getString("@info");
          LOGGER.warn("Upload warning. MediaWiki says: {}: {}", code, info);
        }
      }
    } catch (final IOException e) {
      throw new FailedUploadException(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see comeon.commons.Commons#logout()
   */
  @Override
  public void logout() throws FailedLogoutException {
    synchronized (this) {
      if (this.api.isLoggedIn) {
        LOGGER.debug("Logging out");
        try {
          this.api.logout();
        } catch (final IOException e) {
          throw new FailedLogoutException(e);
        }
      }
    }
  }
  
  @Override
  public String getName() {
    return wiki.getName();
  }
  
}
