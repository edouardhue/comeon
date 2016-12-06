package comeon.ui;

import javax.swing.*;
import java.awt.*;

public abstract class CursorChangingWorker extends SwingWorker<Void, Object> {

    protected CursorChangingWorker() {
        SwingUtilities.invokeLater(() -> {
            UI.findInstance().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        });
    }

    @Override
    protected void done() {
        UI.findInstance().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}
