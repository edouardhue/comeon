package comeon.users;

import java.util.prefs.BackingStoreException;

import comeon.model.User;

public interface Users {

  /**
   * @throws UserNotSetException
   */
  public abstract User getUser() throws UserNotSetException;

  public abstract void setUser(User user) throws BackingStoreException;

}