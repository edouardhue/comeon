package comeon.ui.add;


import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.google.common.io.Resources;
import comeon.templates.Templates;
import comeon.ui.UI;

public final class AddPicturesDialog extends JOptionPane {

  private static final long serialVersionUID = 1L;

  private static final ImageIcon ICON = new ImageIcon(Resources.getResource("comeon/ui/addpictures_huge.png"));

  private final AddModel model;
  
  private final AddController controller;
  
  private final JDialog dialog;
  
  private final AddPicturesPanel filesPanel;
  
  public AddPicturesDialog(final Templates templates) {
    super(null, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, ICON);
    this.controller = new AddController(templates);
    this.model = new AddModel();
    this.controller.registerModel(model);
    this.filesPanel = new AddPicturesPanel(controller);
    this.controller.registerView(filesPanel);
    this.setMessage(this.filesPanel);
    this.dialog = this.createDialog(JOptionPane.getRootFrame(), UI.BUNDLE.getString("action.addpictures.title"));
    this.dialog.setIconImages(UI.ICON_IMAGES);
  }

  public int showDialog() {
    this.dialog.setVisible(true);
    return ((Integer) this.getValue()).intValue();
  }
  
  public AddModel getModel() {
    return model;
  }
}
