package comeon;

import java.util.prefs.Preferences;

import comeon.model.User;

public final class Users {
  
  private User user;
  
  /**
   * @throws UserNotSetException
   */
  public User getUser() {
    if (this.user == null) {
      this.user = loadUser();
    }
    return user;
  }

  /**
   * @throws UserNotSetException
   */
  private User loadUser() {
    final Preferences userPrefs = Preferences.userNodeForPackage(Core.class).node("user");
    final String login = userPrefs.get(PreferencesKeys.LOGIN.name(), null);
    final String password = userPrefs.get(PreferencesKeys.PASSWORD.name(), null);
    final String displayName = userPrefs.get(PreferencesKeys.DISPLAY_NAME.name(), null);
    final User user;
    if (login == null || password == null || displayName == null) {
      // XXX Ugly hack
//      throw new UserNotSetException();
      user = new User("EdouardHue", "xxx", "Édouard Hue");
    } else {
      user = new User(login, password, displayName);
    }
    return user;
  }
  
  private enum PreferencesKeys {
    LOGIN,
    PASSWORD,
    DISPLAY_NAME
  }
}
