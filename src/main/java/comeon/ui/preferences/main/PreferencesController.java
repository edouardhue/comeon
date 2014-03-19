package comeon.ui.preferences.main;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import comeon.ui.preferences.templates.TemplateModel;
import comeon.ui.preferences.wikis.WikiModel;

@Singleton
public final class PreferencesController {
  
  private final PreferencesModel model;

  @Inject
  public PreferencesController(final PreferencesModel model) {
    this.model = model;
  }

  public void add(final TemplateModel model) {
    this.model.getTemplates().addElement(model);
  }
  
  public void add(final WikiModel model) {
    this.model.getWikis().addElement(model);
  }

  public void update(final TemplateModel model, final int index) {
    this.model.update(model, index);
  }
  
  public void update(final WikiModel model, final int index) {
    this.model.update(model, index);
  }
  
  public void removeTemplate(final int index) {
    model.getTemplates().removeElementAt(index);
  }
  
  public void removeWiki(final int index) {
    model.getWikis().removeElementAt(index);
  }
  
  public void persist() {
  }
  
}
