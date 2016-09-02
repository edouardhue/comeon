package comeon.ui.menu;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.swing.*;

@Singleton
public final class MenuBar extends JMenuBar {

    private static final long serialVersionUID = 1L;

    @Inject
    public MenuBar(final FileMenu fileMenu, final EditMenu editMenu, final HelpMenu helpMenu) {
        this.add(fileMenu);
        this.add(editMenu);
        this.add(helpMenu);
    }
}
