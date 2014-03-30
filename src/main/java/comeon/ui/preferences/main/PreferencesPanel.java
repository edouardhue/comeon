package comeon.ui.preferences.main;


import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.UI;

@Singleton
public final class PreferencesPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  @Inject
  public PreferencesPanel(final TemplatesListPanel templatesPanel, final WikisListPanel wikisPanel) {
    final JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
    tabs.addTab(UI.BUNDLE.getString("prefs.tab.templates"), new ImageIcon(Resources.getResource("comeon/ui/template_small.png")), templatesPanel);
    tabs.addTab(UI.BUNDLE.getString("prefs.tab.wikis"), new ImageIcon(Resources.getResource("comeon/ui/wiki_small.png")), wikisPanel);
    this.add(tabs);
  }
  
}
