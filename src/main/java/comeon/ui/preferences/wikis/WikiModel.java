package comeon.ui.preferences.wikis;

import com.google.common.base.Strings;
import comeon.model.User;
import comeon.model.Wiki;
import comeon.ui.preferences.Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public final class WikiModel implements Model {
    private final PropertyChangeSupport pcs;

    private String name;

    private String url;

    private String login;

    private String password;

    private String displayName;

    private Boolean active;

    public enum Properties {
        NAME, URL, LOGIN, PASSWORD, DISPLAY_NAME, ACTIVE
    }

    public WikiModel() {
        this.pcs = new PropertyChangeSupport(this);
        this.active = Boolean.FALSE;
    }

    public WikiModel(final Wiki wiki, final Boolean active) {
        this();
        this.name = wiki.getName();
        this.url = wiki.getUrl();
        this.login = wiki.getUser().getLogin();
        this.password = wiki.getUser().getPassword();
        this.displayName = wiki.getUser().getDisplayName();
        this.active = active;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        final Boolean oldActive = this.active;
        this.active = active;
        pcs.firePropertyChange(Properties.ACTIVE.name(), oldActive, active);
    }

    public Wiki asWiki() {
        return new Wiki(name, url, new User(login, password, displayName));
    }

    public static WikiModel getPrototype() {
        final WikiModel prototype = new WikiModel();
        prototype.name = Strings.repeat("x", 32);
        prototype.displayName = Strings.repeat("xxxxxx ", 3);
        prototype.active = Boolean.TRUE;
        return prototype;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
