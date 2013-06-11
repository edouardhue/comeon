package comeon.ui.preferences;

import java.awt.Frame;
import java.util.prefs.BackingStoreException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import comeon.core.CoreImpl;
import comeon.ui.UI;

public final class PreferencesDialog extends JOptionPane {

  private static final long serialVersionUID = 1L;

  private final JDialog dialog;

  private final JTabbedPane tabs;

  private final UserSettingsPanel userSettingsPanel;

  private final TemplatesPanel templatesPanel;

  public PreferencesDialog(final Frame parent) {
    super(null, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
    this.tabs = new JTabbedPane(JTabbedPane.TOP);
    this.userSettingsPanel = new UserSettingsPanel();
    tabs.add(UI.BUNDLE.getString("prefs.tab.user"), userSettingsPanel);
    this.templatesPanel = new TemplatesPanel(CoreImpl.getInstance().getTemplates().getTemplates());
    tabs.add(UI.BUNDLE.getString("prefs.tab.templates"), templatesPanel);
    this.setMessage(this.tabs);
    this.dialog = this.createDialog(parent, UI.BUNDLE.getString("action.preferences.title"));
  }

  public int showDialog() {
    this.dialog.setVisible(true);
    return ((Integer) this.getValue()).intValue();
  }
  
  public void save() {
    CoreImpl.getInstance().getPool().submit(new Runnable() {
      @Override
      public void run() {
        try {
          CoreImpl.getInstance().getUsers().setUser(userSettingsPanel.getUser());
          CoreImpl.getInstance().getTemplates().setTemplates(templatesPanel.getTemplates());
          CoreImpl.getInstance().getTemplates().storePreferences();
        } catch (final BackingStoreException e) {
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              // TODO i18n
              JOptionPane.showMessageDialog(PreferencesDialog.this.getParent(), e.getLocalizedMessage(), "Error",
                  JOptionPane.ERROR_MESSAGE);
            }
          });
        }
      }
    });
  }
}
