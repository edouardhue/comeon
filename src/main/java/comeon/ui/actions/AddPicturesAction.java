package comeon.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import comeon.UserNotSetException;
import comeon.core.CoreImpl;
import comeon.model.Template;
import comeon.ui.UI;
import comeon.ui.preferences.PreferencesDialog;

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
  
  private TemplateWrapper[] getWrapperTemplates() {
    final List<Template> templates = CoreImpl.getInstance().getTemplates().getTemplates();
    final TemplateWrapper[] wrappers = new TemplateWrapper[templates.size()];
    int i = 0;
    for (final Template template : templates) {
      wrappers[i] = new TemplateWrapper(template);
      i++;
    }
    return wrappers;
  }
  
  @Override
  public void actionPerformed(final ActionEvent e) {
    final int returnVal = chooser.showOpenDialog(ui);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      final File[] files = chooser.getSelectedFiles();
      try {
        final TemplateWrapper[] templates = this.getWrapperTemplates();
        if (templates.length == 0) {
          final String messageKey = "error.notemplates.message";
          final String titleKey = "error.notemplates.title";
          warnAndShowPreferences(messageKey, titleKey);
        } else {
          final TemplateWrapper wrapper = (TemplateWrapper) JOptionPane.showInputDialog(SwingUtilities.getWindowAncestor((Component) e.getSource()), "Choose a template", "Template", JOptionPane.QUESTION_MESSAGE,
              null, templates, templates.length > 0 ? templates[0] : null);
          CoreImpl.getInstance().addPictures(files, wrapper.template);
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              ui.refreshPictures();
            }
          });
        }
      } catch (final UserNotSetException ex) {
        final String messageKey = "error.usernotset.message";
        final String titleKey = "error.usernotset.title";
        warnAndShowPreferences(messageKey, titleKey);
      }
    }
  }

  private void warnAndShowPreferences(final String messageKey, final String titleKey) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        JOptionPane.showMessageDialog(ui, UI.BUNDLE.getString(messageKey),
            UI.BUNDLE.getString(titleKey), JOptionPane.ERROR_MESSAGE);
        new PreferencesDialog(JOptionPane.getRootFrame()).setVisible(true);
      }
    });
  }

  private static final class TemplateWrapper {
    private final Template template;
    
    public TemplateWrapper(final Template template) {
      this.template = template;
    }
    
    @Override
    public String toString() {
      return template.getName();
    }
  }
}
