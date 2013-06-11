package comeon.commons;

import in.yuvi.http.fluent.ProgressListener;

import java.io.IOException;

import comeon.FailedLoginException;
import comeon.FailedLogoutException;
import comeon.FailedUploadException;
import comeon.NotLoggedInException;
import comeon.model.Picture;

public interface Commons {

  public abstract void login() throws NotLoggedInException, FailedLoginException;

  public abstract void upload(Picture picture, ProgressListener listener) throws NotLoggedInException,
      FailedLoginException, FailedUploadException, IOException;

  public abstract void logout() throws FailedLogoutException;

}