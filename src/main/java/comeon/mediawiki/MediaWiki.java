package comeon.mediawiki;

import in.yuvi.http.fluent.ProgressListener;

import java.io.IOException;

import comeon.model.Picture;

public interface MediaWiki {

  public abstract void login() throws NotLoggedInException, FailedLoginException;

  public abstract void upload(Picture picture, ProgressListener listener) throws NotLoggedInException,
      FailedLoginException, FailedUploadException, IOException;

  public abstract void logout() throws FailedLogoutException;

}