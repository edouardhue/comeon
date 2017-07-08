package comeon.ui.actions;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.Core;
import comeon.ui.UI;
import comeon.wikis.Wikis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;

@Singleton
public final class UploadMediaAction extends MediaBaseAction {
    private static final long serialVersionUID = 1L;

    private static final ImageIcon ICON = new ImageIcon(Resources.getResource("comeon/ui/upload_huge.png"));

    private final Wikis wikis;

    @Inject
    public UploadMediaAction(final Core core, final Wikis wikis) {
        super("upload", core);
        this.wikis = wikis;
        this.setEnabled(false);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final int mediaToUpload = core.countMediaToBeUploaded();
        if (mediaToUpload == 0) {
            JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor((Component) e.getSource()),
                    UI.BUNDLE.getString("action.upload.none"));
        } else {
            final int choice = JOptionPane.showConfirmDialog(
                    SwingUtilities.getWindowAncestor((Component) e.getSource()),
                    MessageFormat.format(
                            UI.BUNDLE.getString("action.upload.confirm"),
                            mediaToUpload, wikis.getActiveWiki().getName()),
                    UIManager.getString("OptionPane.titleText"),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    ICON);
            if (JOptionPane.OK_OPTION == choice) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        core.uploadMedia();
                        return null;
                    }
                }.execute();
            }
        }
    }

}
