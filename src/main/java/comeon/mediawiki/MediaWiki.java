package comeon.mediawiki;

import in.yuvi.http.fluent.ProgressListener;

import java.io.IOException;

import comeon.model.Picture;

public interface MediaWiki {

  boolean isLoggedIn();
  
  void login() throws NotLoggedInException, FailedLoginException;

  void upload(Picture picture, ProgressListener listener) throws NotLoggedInException,
      FailedLoginException, FailedUploadException, IOException;

  void logout() throws FailedLogoutException;
  
  String getName();

}