package comeon.ui.actions;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;

import comeon.ui.preferences.PreferencesPanel;

public final class PreferencesAction extends BaseAction {

  private static final long serialVersionUID = 1L;

  public PreferencesAction() {
    super("preferences");
  }
  
  @Override
  public void actionPerformed(final ActionEvent e) {
    final Window root = (Window) SwingUtilities.getRoot((Component) e.getSource());
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new PreferencesPanel(root);
      }
    });
  }

}
