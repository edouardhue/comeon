package comeon.ui.actions;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.Core;
import comeon.ui.UI;

import javax.swing.*;
import javax.swing.text.html.Option;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

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
        final Optional<Window> window = Optional.ofNullable(SwingUtilities.getWindowAncestor((Component) e.getSource()));
        SwingUtilities.invokeLater(() -> {
            final int choice = JOptionPane.showConfirmDialog(
                    window.orElse(null),
                    UI.BUNDLE.getString("action.clearmedia.confirm"),
                    UI.BUNDLE.getString("action.clearmedia.title"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    ICON
            );
            if (JOptionPane.YES_OPTION == choice) {
                window.ifPresent(w -> w.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)));
                final SwingWorker<Void, Object> worker = new SwingWorker<Void, Object>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        core.removeAllMedia();
                        return null;
                    }
                };
                worker.addPropertyChangeListener(evt -> {
                    if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                        SwingUtilities.invokeLater(() -> window.ifPresent(w -> w.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))));
                    }
                });
                worker.execute();
            }
        });
    }
}
