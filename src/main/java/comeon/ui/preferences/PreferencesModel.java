package comeon.ui.preferences;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

final class PreferencesModel {
  private ListModel<TemplateModel> templatesModel;
  
  private ListModel<WikiModel> wikisModel;
  
  public PreferencesModel() {
    this.templatesModel = new DefaultListModel<>();
    this.wikisModel = new DefaultListModel<>();
  }

  public ListModel<TemplateModel> getTemplates() {
    return templatesModel;
  }
  
  public ListModel<WikiModel> getWikis() {
    return wikisModel;
  }
}
