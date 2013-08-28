package comeon.core;

import java.util.prefs.BackingStoreException;

public interface WithPreferences<E extends Exception> {
  void loadPreferences() throws BackingStoreException, E;
}
