package comeon.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.preferences.main.PreferencesController;
import comeon.ui.preferences.main.PreferencesDialog;

@Singleton
public final class PreferencesAction extends BaseAction {

  private static final long serialVersionUID = 1L;

  private final PreferencesDialog dialog;

  private final PreferencesController controller;
  
  @Inject
  public PreferencesAction(final PreferencesDialog dialog, final PreferencesController controller) {
    super("preferences");
    this.dialog = dialog;
    this.controller = controller;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        final int value = dialog.showDialog();
        if (value == JOptionPane.OK_OPTION) {
          controller.persist();
        }
      }
    });
  }

}
