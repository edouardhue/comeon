package comeon.ui.preferences;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comeon.model.Wiki;
import comeon.templates.Templates;
import comeon.ui.UI;
import comeon.wikis.Wikis;

public final class PreferencesDialog extends JOptionPane {

  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(PreferencesDialog.class);

  private final JDialog dialog;

  private final JTabbedPane tabs;

  public PreferencesDialog(final Templates templates, final Wikis wikis) {
    super(null, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
    this.tabs = new JTabbedPane(SwingConstants.TOP);
    final List<Wiki> wikisList = wikis.getWikis();
    assert wikisList.contains(wikis.getActiveWiki());
    this.setMessage(this.tabs);
    this.dialog = this.createDialog(JOptionPane.getRootFrame(), UI.BUNDLE.getString("action.preferences.title"));
  }

  public int showDialog() {
    this.dialog.setVisible(true);
    return ((Integer) this.getValue()).intValue();
  }

  public void save() {
  }
}
