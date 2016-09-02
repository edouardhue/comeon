package comeon.ui.actions;

import comeon.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class HelpManualAction extends BaseAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelpManualAction.class);

    private static final long serialVersionUID = 1L;

    public HelpManualAction() throws URISyntaxException {
        super("manual", KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        try {
            final URI manualUri = new URI(UI.BUNDLE.getString("manual.url"));
            if (Desktop.isDesktopSupported()) {
                final Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                desktop.browse(manualUri);
                            } catch (final IOException ex) {
                                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), ex.getLocalizedMessage(), UI.BUNDLE.getString("error.generic.title"), JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                }
            }
        } catch (final URISyntaxException ex) {
            LOGGER.error("Malformed help manual URI", ex);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), ex.getLocalizedMessage(), UI.BUNDLE.getString("error.generic.title"), JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

}
