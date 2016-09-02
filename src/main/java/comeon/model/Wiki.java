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

    @Override
    public boolean equals(final Object obj) {
        final boolean isEqual;

        if (obj == null) {
            isEqual = false;
        } else if (obj instanceof Wiki) {
            isEqual = this.name.equals(((Wiki) obj).name);
        } else {
            isEqual = false;
        }

        return isEqual;
    }
}
