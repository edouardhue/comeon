package comeon.ui.preferences;

import java.util.ArrayList;
import java.util.List;

import comeon.model.Template;
import comeon.model.Wiki;
import comeon.templates.Templates;
import comeon.wikis.Wikis;


public final class PreferencesController {
  
  private PreferencesModel model;
  
  private final Templates templates;
  
  private final Wikis wikis;
  
  public PreferencesController(final Templates templates, final Wikis wikis) {
    this.templates = templates;
    this.wikis = wikis;
  }
  
  public void registerModel(final PreferencesModel model) {
    this.model = model;
    this.model.updateModel(getTemplateModels(), getWikiModels());
  }
  
  private List<TemplateModel> getTemplateModels() {
    final List<Template> templates = this.templates.getTemplates();
    final List<TemplateModel> templateModels = new ArrayList<>(templates.size());
    for (final Template template : templates) {
      templateModels.add(new TemplateModel(template));
    }
    return templateModels;
  }
  
  private List<WikiModel> getWikiModels() {
    final List<Wiki> wikis = this.wikis.getWikis();
    final List<WikiModel> wikiModels = new ArrayList<>(wikis.size());
    for (final Wiki wiki : wikis) {
      wikiModels.add(new WikiModel(wiki));
    }
    return wikiModels;
    
  }
  
  public void registerView(final PreferencesPanel view) {
    view.updateModels(model.getTemplates(), model.getWikis());
  }

  public void persist() {
    
  }
  
}
