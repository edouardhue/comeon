package comeon.ui.actions;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.Core;
import comeon.ui.CursorChangingWorker;
import comeon.ui.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Singleton
public class ClearMediaAction extends MediaBaseAction {

    private static final long serialVersionUID = 1L;

    private static final ImageIcon ICON = new ImageIcon(Resources.getResource("comeon/ui/clearmedia_huge.png"));

    @Inject
    protected ClearMediaAction(final Core core) {
        super("clearmedia", core);
        this.setEnabled(false);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            final int choice = JOptionPane.showConfirmDialog(
                    UI.findInstance(),
                    UI.BUNDLE.getString("action.clearmedia.confirm"),
                    UI.BUNDLE.getString("action.clearmedia.title"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    ICON
            );
            if (JOptionPane.YES_OPTION == choice) {
                new RemovalWorker().execute();
            }
        });
    }

    private class RemovalWorker extends CursorChangingWorker {

        @Override
        protected Void doInBackground() throws Exception {
            core.removeAllMedia();
            return null;
        }

    }
}
