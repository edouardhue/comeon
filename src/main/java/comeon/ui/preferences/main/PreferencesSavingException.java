package comeon.ui.preferences.main;

import java.util.List;

public final class PreferencesSavingException extends Exception {
  private static final long serialVersionUID = 1L;
  
  private final List<Exception> causes;
  
  public PreferencesSavingException(final List<Exception> causes) {
    this.causes = causes;
  }
  
  public List<Exception> getCauses() {
    return causes;
  }
}
