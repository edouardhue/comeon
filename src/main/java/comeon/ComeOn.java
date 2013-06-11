package comeon;

import java.io.IOException;
import java.util.prefs.BackingStoreException;

import javax.swing.SwingUtilities;

import comeon.core.CoreImpl;
import comeon.ui.UI;

public final class ComeOn {

  public static void main(final String[] args) throws IOException, UserNotSetException, BackingStoreException {
    final CoreImpl core = CoreImpl.getInstance();
    core.getTemplates().readPreferences();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new UI();
      }
    });
  }

}
