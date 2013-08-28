package comeon.users;

import java.util.prefs.BackingStoreException;

import comeon.model.User;

public interface Users {

  public abstract User getUser();

  public abstract void setUser(User user) throws BackingStoreException;

}