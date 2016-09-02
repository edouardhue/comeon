package comeon.mediawiki;

public final class NotLoggedInException extends MediaWikiException {

    private static final long serialVersionUID = 1L;

    private final String loginResult;

    public NotLoggedInException(final String loginResult) {
        super();
        this.loginResult = loginResult;
    }

    public String getLoginResult() {
        return loginResult;
    }

}
