package comeon.ui;

import javax.swing.*;
import java.awt.*;

public abstract class CursorChangingWorker extends SwingWorker<Void, Object> {

    protected CursorChangingWorker() {
        final Window window = UI.findInstance();
        window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName()) && StateValue.DONE.equals(evt.getNewValue())) {
                SwingUtilities.invokeLater(() -> window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)));
            }
        });
    }
}
