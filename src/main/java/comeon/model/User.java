package comeon.model;

public final class User {
    private String login;

    private String password;

    private String displayName;

    public User(final String login, final String password, final String displayName) {
        super();
        this.login = login;
        this.password = password;
        this.displayName = displayName;
    }

    public User(final User template) {
        this.login = template.login;
        this.password = template.password;
        this.displayName = template.displayName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
}
