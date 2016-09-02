package comeon.ui.menu;

import comeon.ui.UI;

import javax.swing.*;

abstract class BaseMenu extends JMenu {

    private static final long serialVersionUID = 1L;

    BaseMenu(final String bundleKey) {
        super(UI.BUNDLE.getString("menu." + bundleKey + ".title"));
        final char mnemo = UI.BUNDLE.getString("menu." + bundleKey + ".mnemo").charAt(0);
        this.setMnemonic(mnemo);
    }

}