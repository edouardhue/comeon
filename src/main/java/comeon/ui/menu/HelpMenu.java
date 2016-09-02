package comeon.ui.menu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.actions.AboutAction;
import comeon.ui.actions.HelpManualAction;

import javax.swing.*;

@Singleton
public final class HelpMenu extends BaseMenu {
    private static final long serialVersionUID = 1L;

    @Inject
    public HelpMenu(final HelpManualAction helpManualAction, final AboutAction aboutAction) {
        super("help");
        this.add(new JMenuItem(helpManualAction));
        this.add(new JSeparator());
        this.add(new JMenuItem(aboutAction));
    }
}