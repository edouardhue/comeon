package comeon.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import comeon.templates.Templates;
import comeon.ui.UI;
import comeon.ui.preferences.PreferencesDialog;
import comeon.users.Users;

public final class PreferencesAction extends BaseAction {

  private static final long serialVersionUID = 1L;

  private final Users users;
  
  private final Templates templates;
  
  public PreferencesAction(final UI ui, final Users users, final Templates templates) {
    super("preferences", ui);
    this.users = users;
    this.templates = templates;
  }
  
  @Override
  public void actionPerformed(final ActionEvent e) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        final PreferencesDialog dialog = new PreferencesDialog(users, templates);
        final int value = dialog.showDialog();
        if (value == JOptionPane.OK_OPTION) {
          dialog.save();
        }
      }
    });
  }

}
