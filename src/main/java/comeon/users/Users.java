package comeon.users;

import java.util.prefs.BackingStoreException;

import comeon.core.WithPreferences;
import comeon.model.User;

public interface Users extends WithPreferences<UserNotSetException> {

  public abstract User getUser();

  public abstract void setUser(User user) throws BackingStoreException;

}