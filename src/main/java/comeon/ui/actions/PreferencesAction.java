package comeon.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import comeon.templates.Templates;
import comeon.ui.preferences.main.PreferencesDialog;
import comeon.wikis.Wikis;

public final class PreferencesAction extends BaseAction {

  private static final long serialVersionUID = 1L;

  private final Templates templates;
  
  private final Wikis wikis;

  public PreferencesAction(final Templates templates, final Wikis wikis) {
    super("preferences");
    this.templates = templates;
    this.wikis = wikis;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        final PreferencesDialog dialog = new PreferencesDialog(templates, wikis);
        final int value = dialog.showDialog();
        if (value == JOptionPane.OK_OPTION) {
          dialog.save();
        }
      }
    });
  }

}
