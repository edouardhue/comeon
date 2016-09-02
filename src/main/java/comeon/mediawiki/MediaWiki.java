package comeon.mediawiki;

import comeon.model.Media;
import in.yuvi.http.fluent.ProgressListener;

import java.io.IOException;

public interface MediaWiki {

    boolean isLoggedIn();

    void login() throws NotLoggedInException, FailedLoginException;

    void upload(Media media, ProgressListener listener) throws NotLoggedInException,
            FailedLoginException, FailedUploadException, IOException;

    void logout() throws FailedLogoutException;

    String getName();

}