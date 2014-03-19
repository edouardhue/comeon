package comeon.ui.preferences.main;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.UI;

@Singleton
public final class PreferencesDialog extends JOptionPane {

  private static final long serialVersionUID = 1L;
  
  private final JDialog dialog;
  
  @Inject
  public PreferencesDialog(final PreferencesPanel panel) {
    super(null, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
    this.setMessage(panel);
    this.dialog = this.createDialog(JOptionPane.getRootFrame(), UI.BUNDLE.getString("action.preferences.title"));
  }

  public int showDialog() {
    this.dialog.setVisible(true);
    return ((Integer) this.getValue()).intValue();
  }
}
