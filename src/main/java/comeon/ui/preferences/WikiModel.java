package comeon.ui.preferences;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.google.common.base.Strings;
import comeon.model.Wiki;

public final class WikiModel implements Model {
  private final PropertyChangeSupport pcs;

  private String name;

  private String url;

  private String login;

  private String password;

  private String displayName;

  public enum Properties {
    NAME, URL, LOGIN, PASSWORD, DISPLAY_NAME
  }

  public WikiModel() {
    this.pcs = new PropertyChangeSupport(this);
  }
  
  public WikiModel(final Wiki wiki) {
    this();
    this.name = wiki.getName();
    this.url = wiki.getUrl();
    this.login = wiki.getUser().getLogin();
    this.password = wiki.getUser().getPassword();
    this.displayName = wiki.getUser().getDisplayName();
  }

  @Override
  public void addPropertyChangeListener(final PropertyChangeListener pcl) {
    this.pcs.addPropertyChangeListener(pcl);
  }

  @Override
  public void removePropertyChangeListener(final PropertyChangeListener pcl) {
    this.pcs.removePropertyChangeListener(pcl);
  }
  
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    final String oldName = this.name;
    this.name = name;
    pcs.firePropertyChange(Properties.NAME.name(), oldName, name);
  }
  
  public String getUrl() {
    return url;
  }
  
  public void setUrl(final String url) {
    final String oldUrl = this.url;
    this.url = url;
    pcs.firePropertyChange(Properties.URL.name(), oldUrl, url);
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(final String login) {
    final String oldLogin = this.login;
    this.login = login;
    pcs.firePropertyChange(Properties.LOGIN.name(), oldLogin, login);
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    final String oldPassword = this.password;
    this.password = password;
    pcs.firePropertyChange(Properties.PASSWORD.name(), oldPassword, password);
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(final String displayName) {
    final String oldDisplayName = this.displayName;
    this.displayName = displayName;
    pcs.firePropertyChange(Properties.DISPLAY_NAME.name(), oldDisplayName, displayName);
  }

  static WikiModel getPrototype() {
    final WikiModel prototype = new WikiModel();
    prototype.name = Strings.repeat("x", 32);
    prototype.displayName = Strings.repeat("xxxxxx ", 3);
    return prototype;
  }
  
  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
