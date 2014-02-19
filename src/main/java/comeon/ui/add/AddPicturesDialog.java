package comeon.ui.add;


import javax.swing.JDialog;
import javax.swing.JOptionPane;

import comeon.ui.UI;

public final class AddPicturesDialog extends JOptionPane {

  private static final long serialVersionUID = 1L;

  private final AddModel model;
  
  private final AddController controller;
  
  private final JDialog dialog;
  
  private final FilesPanel filesPanel;
  
  public AddPicturesDialog() {
    super(null, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
    this.controller = new AddController();
    this.model = new AddModel();
    this.controller.registerModel(model);
    this.filesPanel = new FilesPanel(controller);
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
