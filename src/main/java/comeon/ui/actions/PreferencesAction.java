package comeon.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;

import comeon.ui.UI;
import comeon.ui.preferences.PreferencesPanel;

public final class PreferencesAction extends BaseAction {

  private static final long serialVersionUID = 1L;

  public PreferencesAction(final UI ui) {
    super("preferences", ui);
  }
  
  @Override
  public void actionPerformed(final ActionEvent e) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        final PreferencesPanel panel = new PreferencesPanel(ui);
        panel.setVisible(true);
      }
    });
  }

}
