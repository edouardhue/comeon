package comeon.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import comeon.Core;
import comeon.UserNotSetException;
import comeon.model.Template;
import comeon.model.TemplateKind;
import comeon.ui.UI;
import comeon.ui.preferences.PreferencesPanel;

public final class AddPicturesAction extends BaseAction {

  private static final long serialVersionUID = 1L;
  
  private final JFileChooser chooser;

  public AddPicturesAction(final UI ui) {
    super("addpictures", ui);
    this.chooser = new JFileChooser();
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setMultiSelectionEnabled(true);
    chooser.setFileFilter(new FileNameExtensionFilter("JPEG files", "jpg", "jpeg"));
  }
  
  @Override
  public void actionPerformed(final ActionEvent e) {
    final int returnVal = chooser.showOpenDialog(ui);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      final File[] files = chooser.getSelectedFiles();
      try {
        // TODO use a real configured template
        final String templateText = Files.toString(UI.TEMPLATE_FILE, Charsets.UTF_8);
        final Template template = new Template("DEFAULT", "DEFAULT", UI.TEMPLATE_FILE, templateText, TemplateKind.VELOCITY);
        Core.getInstance().addPictures(files, template);
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            ui.refreshPictures();
          }
        });
      } catch (final UserNotSetException ex) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            JOptionPane.showMessageDialog(ui, UI.BUNDLE.getString("error.usernotset.message"),
                UI.BUNDLE.getString("error.usernotset.title"), JOptionPane.ERROR_MESSAGE);
            new PreferencesPanel(JOptionPane.getRootFrame()).setVisible(true);
          }
        });
      } catch (final IOException ex) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            JOptionPane.showMessageDialog(ui, UI.BUNDLE.getString("error.unreadabletemplate.message"),
                UI.BUNDLE.getString("error.unreadabletemplate.title"), JOptionPane.ERROR_MESSAGE);
          }
        });
      }
    }
  }

}
