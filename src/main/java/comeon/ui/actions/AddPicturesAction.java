package comeon.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.Core;
import comeon.model.Template;
import comeon.templates.Templates;
import comeon.templates.TemplatesChangedEvent;
import comeon.ui.UI;
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

  private TemplateWrapper[] getWrapperTemplates() {
    final List<Template> templates = this.templates.getTemplates();
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
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        final AddPicturesDialog dialog = new AddPicturesDialog();
        final int value = dialog.showDialog();
        if (value == JOptionPane.OK_OPTION) {
          final File[] files = dialog.getPicturesFiles();
          if (files.length > 0) {
            final TemplateWrapper[] templates = getWrapperTemplates();
            final TemplateWrapper wrapper = (TemplateWrapper) JOptionPane.showInputDialog(
                SwingUtilities.getWindowAncestor((Component) e.getSource()),
                UI.BUNDLE.getString("action.addpictures.choosetemplate.message"),
                UI.BUNDLE.getString("action.addpictures.choosetemplate.title"),
                JOptionPane.QUESTION_MESSAGE,
                null,
                templates, templates.length > 0 ? templates[0] : null);
            core.addPictures(files, wrapper.template);
          }
        }
      }
    });
  }
  
  @Subscribe
  public void handleTemplatesChanged(final TemplatesChangedEvent event) {
    if (event.getTemplates().isEmpty()) {
      this.setEnabled(false);
    } else {
      this.setEnabled(true);
    }
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
