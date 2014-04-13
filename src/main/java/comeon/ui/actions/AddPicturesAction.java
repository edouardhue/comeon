package comeon.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.Core;
import comeon.templates.Templates;
import comeon.templates.TemplatesChangedEvent;
import comeon.ui.add.AddModel;
import comeon.ui.add.AddPicturesDialog;

@Singleton
public final class AddPicturesAction extends BaseAction {

  private static final long serialVersionUID = 1L;
  
  private final Templates templates;

  private final Core core;

  @Inject
  public AddPicturesAction(final Templates templates, final Core core) {
    super("addpictures");
    this.templates = templates;
    this.core = core;
    if (templates.getTemplates().isEmpty()) {
      this.setEnabled(false);
    }
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        final AddPicturesDialog dialog = new AddPicturesDialog(templates);
        final int value = dialog.showDialog();
        if (value == JOptionPane.OK_OPTION) {
          final AddModel model = dialog.getModel();
          final File[] files = model.getPicturesFiles();
          if (files.length > 0) {
            core.addPictures(files, model.getTemplate(), model.getExternalMetadataSource());
          }
        }
      }
    });
  }
  
  @Subscribe
  public void handleTemplatesChanged(final TemplatesChangedEvent event) {
    this.setEnabled(!event.getTemplates().isEmpty());
  }
}
