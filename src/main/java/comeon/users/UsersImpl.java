package comeon.users;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import comeon.core.CoreImpl;
import comeon.model.User;

public final class UsersImpl implements Users {
  
  private User user;
  
  /* (non-Javadoc)
   * @see comeon.users.Users#getUser()
   */
  @Override
  public User getUser() throws UserNotSetException {
    if (this.user == null) {
      this.user = loadUser();
    }
    return user;
  }
  
  /* (non-Javadoc)
   * @see comeon.users.Users#setUser(comeon.model.User)
   */
  @Override
  public void setUser(final User user) throws BackingStoreException {
    this.user = user;
    final Preferences userPrefs = getUserPreferences();
    userPrefs.put(PreferencesKeys.LOGIN.name(), user.getLogin());
    userPrefs.put(PreferencesKeys.PASSWORD.name(), user.getPassword());
    userPrefs.put(PreferencesKeys.DISPLAY_NAME.name(), user.getDisplayName());
    userPrefs.flush();
  }

  /**
   * @throws UserNotSetException
   */
  private User loadUser() throws UserNotSetException {
    final Preferences userPrefs = getUserPreferences();
    final String login = userPrefs.get(PreferencesKeys.LOGIN.name(), null);
    final String password = userPrefs.get(PreferencesKeys.PASSWORD.name(), null);
    final String displayName = userPrefs.get(PreferencesKeys.DISPLAY_NAME.name(), null);
    final User user;
    if (login == null || password == null || displayName == null) {
      throw new UserNotSetException();
    } else {
      user = new User(login, password, displayName);
    }
    return user;
  }

  private Preferences getUserPreferences() {
    return Preferences.userNodeForPackage(CoreImpl.class).node("user");
  }
  
  private enum PreferencesKeys {
    LOGIN,
    PASSWORD,
    DISPLAY_NAME
  }
}
