package comeon.ui.preferences;

import java.util.List;
import java.util.prefs.BackingStoreException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

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

  private final TemplatesPanel templatesPanel;
  
  private final WikisPanel wikisPanel;

  private final Templates templates;
  
  private final Wikis wikis;

  public PreferencesDialog(final Templates templates, final Wikis wikis) {
    super(null, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
    this.templates = templates;
    this.wikis = wikis;
    this.tabs = new JTabbedPane(SwingConstants.TOP);
    this.templatesPanel = new TemplatesPanel(templates);
    tabs.add(UI.BUNDLE.getString("prefs.tab.templates"), templatesPanel);
    final List<Wiki> wikisList = wikis.getWikis();
    assert wikisList.contains(wikis.getActiveWiki());
    this.wikisPanel = new WikisPanel(wikisList, wikisList.indexOf(wikis.getActiveWiki()));
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
      templates.setTemplates(templatesPanel.getTemplates());
      templates.save();
      wikis.setWikis(wikisPanel.getWikis());
      wikis.setActiveWiki(wikisPanel.getActiveWiki());
      wikis.save();
    } catch (final BackingStoreException e) {
      LOGGER.error(UI.BUNDLE.getString("prefs.error.save"), e);
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          JOptionPane.showMessageDialog(
              PreferencesDialog.this.getParent(),
              new StringBuffer(UI.BUNDLE.getString("prefs.error.save")).append('\n').append(e.getLocalizedMessage()),
              UI.BUNDLE.getString("error.generic.title"),
              JOptionPane.ERROR_MESSAGE);
        }
      });
    }
  }
}
