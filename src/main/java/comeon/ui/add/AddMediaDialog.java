package comeon.ui.add;


import com.google.common.io.Resources;
import comeon.templates.Templates;
import comeon.ui.UI;

import javax.swing.*;
import java.io.File;

public final class AddMediaDialog extends JOptionPane {

    private static final long serialVersionUID = 1L;

    private static final ImageIcon ICON = new ImageIcon(Resources.getResource("comeon/ui/addmedia_huge.png"));

    private final AddModel model;

    private final AddController controller;

    private final JDialog dialog;

    private final AddMediaPanel mediaPanel;

    public AddMediaDialog(final Templates templates) {
        this(templates, new File[0]);
    }

    public AddMediaDialog(final Templates templates, final File[] preselectedFiles) {
        super(null, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, ICON);
        this.controller = new AddController(templates);
        this.model = new AddModel();
        this.controller.registerModel(model);
        this.mediaPanel = new AddMediaPanel(controller);
        this.controller.registerView(mediaPanel);
        this.setMessage(this.mediaPanel);
        this.dialog = this.createDialog(JOptionPane.getRootFrame(), UI.BUNDLE.getString("action.addmedia.title"));
        this.dialog.setIconImages(UI.ICON_IMAGES);
        this.model.setMediaFiles(preselectedFiles);
    }

    public int showDialog() {
        this.dialog.setVisible(true);
        return ((Integer) this.getValue()).intValue();
    }

    public AddModel getModel() {
        return model;
    }
}
