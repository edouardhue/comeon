package comeon.mediawiki;

import in.yuvi.http.fluent.ProgressListener;

import java.io.IOException;

import comeon.model.Media;

public interface MediaWiki {

  boolean isLoggedIn();
  
  void login() throws NotLoggedInException, FailedLoginException;

  void upload(Media media, ProgressListener listener) throws NotLoggedInException,
      FailedLoginException, FailedUploadException, IOException;

  void logout() throws FailedLogoutException;
  
  String getName();

}