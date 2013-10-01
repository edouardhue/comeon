package comeon.model;

public final class Wiki {
  private final String name;
  
  private final String url;
  
  private final User user;
  
  public Wiki(final String name, final String url, final User user) {
    this.name = name;
    this.url = url;
    this.user = user;
  }
  
  public String getName() {
    return name;
  }
  
  public String getUrl() {
    return url;
  }
  
  public User getUser() {
    return user;
  }
}
