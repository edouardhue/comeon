package comeon.model;

public final class Wiki {
  private final String name;
  
  private final String url;
  
  public Wiki(final String name, final String url) {
    this.name = name;
    this.url = url;
  }
  
  public String getName() {
    return name;
  }
  
  public String getUrl() {
    return url;
  }
}
