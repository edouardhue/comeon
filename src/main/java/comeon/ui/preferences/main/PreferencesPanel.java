package comeon.ui.preferences.main;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.UI;

import javax.swing.*;

@Singleton
public final class PreferencesPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    @Inject
    public PreferencesPanel(final TemplatesListPanel templatesPanel, final WikisListPanel wikisPanel) {
        final JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.addTab(UI.BUNDLE.getString("prefs.tab.templates"), templatesPanel);
        tabs.addTab(UI.BUNDLE.getString("prefs.tab.wikis"), wikisPanel);
        this.add(tabs);
    }

}
