package comeon.users;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ComeOn;
import comeon.core.WithPreferences;
import comeon.model.User;

@Singleton
public final class UsersImpl implements Users, WithPreferences<UserNotSetException> {

  private User user;

  @Inject
  private UsersImpl() {
  }

  @Override
  public User getUser() {
    return user;
  }

  @Override
  public void setUser(final User user) throws BackingStoreException {
    this.user = user;
    final Preferences userPrefs = getUserPreferences();
    userPrefs.put(PreferencesKeys.LOGIN.name(), user.getLogin());
    userPrefs.put(PreferencesKeys.PASSWORD.name(), user.getPassword());
    userPrefs.put(PreferencesKeys.DISPLAY_NAME.name(), user.getDisplayName());
    userPrefs.flush();
  }

  
  @Override
  public void loadPreferences() throws BackingStoreException, UserNotSetException {
    final Preferences userPrefs = getUserPreferences();
    final String login = userPrefs.get(PreferencesKeys.LOGIN.name(), null);
    final String password = userPrefs.get(PreferencesKeys.PASSWORD.name(), null);
    final String displayName = userPrefs.get(PreferencesKeys.DISPLAY_NAME.name(), null);
    if (login == null || password == null || displayName == null) {
      throw new UserNotSetException();
    } else {
      user = new User(login, password, displayName);
    }
  }

  private Preferences getUserPreferences() {
    return Preferences.userNodeForPackage(ComeOn.class).node("user");
  }

  private enum PreferencesKeys {
    LOGIN, PASSWORD, DISPLAY_NAME
  }
}
