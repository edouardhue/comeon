package comeon;

public final class NotLoggedInException extends CommonsException {

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
