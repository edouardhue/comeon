package comeon.ui.preferences;

import java.util.List;

import javax.swing.DefaultListModel;

final class PreferencesModel {
  private DefaultListModel<TemplateModel> templatesModel;
  
  private DefaultListModel<WikiModel> wikisModel;
  
  public PreferencesModel() {
    this.templatesModel = new DefaultListModel<>();
    this.wikisModel = new DefaultListModel<>();
  }

  void updateModel(final List<TemplateModel> templates, final List<WikiModel> wikis) {
    updateModel(templates, this.templatesModel);
    updateModel(wikis, this.wikisModel);
  }
  
  private static <M> void updateModel(final List<M> data, final DefaultListModel<M> listModel) {
    listModel.removeAllElements();
    listModel.ensureCapacity(data.size());
    for (final M element : data) {
      listModel.addElement(element);
    }
    listModel.trimToSize();
  }
  
  public DefaultListModel<TemplateModel> getTemplates() {
    return templatesModel;
  }
  
  public DefaultListModel<WikiModel> getWikis() {
    return wikisModel;
  }
}
