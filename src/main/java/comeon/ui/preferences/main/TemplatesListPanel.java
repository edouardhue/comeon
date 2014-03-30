package comeon.ui.preferences.main;

import javax.swing.ImageIcon;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.preferences.templates.TemplateListCellRenderer;
import comeon.ui.preferences.templates.TemplateModel;
import comeon.ui.preferences.templates.TemplateSubController;
import comeon.ui.preferences.templates.TemplateSubPanel;

@Singleton
public final class TemplatesListPanel extends ListPanel<TemplateModel> {
  private static final long serialVersionUID = 1L;

  @Inject
  public TemplatesListPanel(final TemplateSubController subController, final TemplateSubPanel subPanel) {
    super(new TemplateListCellRenderer(), subController, subPanel, subController.getMainController().getTemplates(),
        "templates", new ImageIcon(Resources.getResource("comeon/ui/template_large.png")), TemplateModel.getPrototype());
  }
}