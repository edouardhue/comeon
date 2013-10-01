package comeon.core;

import java.util.prefs.BackingStoreException;

public interface WithPreferences {
  void loadPreferences() throws BackingStoreException;
}
