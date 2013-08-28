package comeon.ui.preferences;

import java.util.prefs.BackingStoreException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import comeon.templates.Templates;
import comeon.ui.UI;
import comeon.users.Users;
import comeon.wikis.Wikis;

public final class PreferencesDialog extends JOptionPane {

  private static final long serialVersionUID = 1L;

  private final JDialog dialog;

  private final JTabbedPane tabs;

  private final UserSettingsPanel userSettingsPanel;

  private final TemplatesPanel templatesPanel;
  
  private final WikisPanel wikisPanel;

  private final Users users;

  private final Templates templates;
  
  private final Wikis wikis;

  public PreferencesDialog(final Users users, final Templates templates, final Wikis wikis) {
    super(null, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
    this.users = users;
    this.templates = templates;
    this.wikis = wikis;
    this.tabs = new JTabbedPane(SwingConstants.TOP);
    this.userSettingsPanel = new UserSettingsPanel(users);
    tabs.add(UI.BUNDLE.getString("prefs.tab.user"), userSettingsPanel);
    this.templatesPanel = new TemplatesPanel(templates.getTemplates());
    tabs.add(UI.BUNDLE.getString("prefs.tab.templates"), templatesPanel);
    this.wikisPanel = new WikisPanel(wikis.getWikis());
    tabs.add(UI.BUNDLE.getString("prefs.tab.wikis"), wikisPanel);
    this.setMessage(this.tabs);
    this.dialog = this.createDialog(JOptionPane.getRootFrame(), UI.BUNDLE.getString("action.preferences.title"));
  }

  public int showDialog() {
    this.dialog.setVisible(true);
    return ((Integer) this.getValue()).intValue();
  }

  public void save() {
    try {
      users.setUser(userSettingsPanel.getUser());
      templates.setTemplates(templatesPanel.getTemplates());
      templates.save();
      wikis.setWikis(wikisPanel.getWikis());
      wikis.save();
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
}
