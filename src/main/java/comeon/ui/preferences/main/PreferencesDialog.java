package comeon.ui.preferences.main;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.UI;

import javax.swing.*;

@Singleton
public final class PreferencesDialog extends JOptionPane {

    private static final long serialVersionUID = 1L;

    private static final ImageIcon ICON = new ImageIcon(Resources.getResource("comeon/ui/preferences_huge.png"));

    private final JDialog dialog;

    @Inject
    public PreferencesDialog(final PreferencesPanel panel) {
        super(null, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, ICON);
        this.setMessage(panel);
        this.dialog = this.createDialog(JOptionPane.getRootFrame(), UI.BUNDLE.getString("action.preferences.title"));
        this.dialog.setIconImages(UI.ICON_IMAGES);
    }

    public int showDialog() {
        this.dialog.setVisible(true);
        return ((Integer) this.getValue()).intValue();
    }
}
