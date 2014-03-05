package comeon.ui.preferences;

import java.util.ArrayList;
import java.util.List;

import comeon.model.Template;
import comeon.model.Wiki;
import comeon.templates.Templates;
import comeon.wikis.Wikis;


public final class PreferencesController {
  
  private final Templates templates;
  
  private final TemplateSubController templateSubController;
  
  private final Wikis wikis;
  
  private final WikiSubController wikiSubController;
  
  private PreferencesModel model;

  public PreferencesController(final Templates templates, final Wikis wikis) {
    this.templates = templates;
    this.templateSubController = new TemplateSubController(this);
    this.wikiSubController = new WikiSubController(this);
    this.wikis = wikis;
  }
  
  public TemplateSubController getTemplateSubController() {
    return templateSubController;
  }
  
  public WikiSubController getWikiSubController() {
    return wikiSubController;
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
  
  public void add(final TemplateModel model) {
    this.model.getTemplates().addElement(model);
  }
  
  public void add(final WikiModel model) {
    this.model.getWikis().addElement(model);
  }
  
  public void removeTemplate(final int index) {
    model.getTemplates().removeElementAt(index);
  }
  
  public void removeWiki(final int index) {
    model.getWikis().removeElementAt(index);
  }
  
  public void registerView(final PreferencesPanel view) {
    view.updateModels(model.getTemplates(), model.getWikis());
    view.setController(this);
    templateSubController.registerView(view.getTemplateSubPanel());
    wikiSubController.registerView(view.getWikiSubPanel());
  }
  
  public void persist() {
    
  }
  
}
