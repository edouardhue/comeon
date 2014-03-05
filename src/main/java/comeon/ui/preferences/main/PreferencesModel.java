package comeon.ui.preferences.main;

import java.util.List;

import javax.swing.DefaultListModel;

import comeon.ui.preferences.templates.TemplateModel;
import comeon.ui.preferences.wikis.WikiModel;

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
  
  public void update(final TemplateModel model, final int index) {
    this.templatesModel.set(index, model);
  }
  
  public void update(final WikiModel model, final int index) {
    this.wikisModel.set(index, model);
  }
  
  public DefaultListModel<WikiModel> getWikis() {
    return wikisModel;
  }
}
