package comeon.ui.menu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.actions.PreferencesAction;

import javax.swing.*;

@Singleton
public final class EditMenu extends BaseMenu {
    private static final long serialVersionUID = 1L;

    @Inject
    public EditMenu(final PreferencesAction action) {
        super("edit");
        this.add(new JMenuItem(action));
    }
}