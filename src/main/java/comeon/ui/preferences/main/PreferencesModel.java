package comeon.ui.preferences.main;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import comeon.model.Template;
import comeon.model.Wiki;
import comeon.templates.Templates;
import comeon.ui.preferences.templates.TemplateModel;
import comeon.ui.preferences.wikis.WikiModel;
import comeon.wikis.Wikis;

@Singleton
public final class PreferencesModel {
  private DefaultListModel<TemplateModel> templatesModel;
  
  private DefaultListModel<WikiModel> wikisModel;

  @Inject
  public PreferencesModel(final Templates templates, final Wikis wikis) {
    this.templatesModel = new DefaultListModel<>();
    this.wikisModel = new DefaultListModel<>();
    updateModels(getTemplateModels(templates.getTemplates()), getWikiModels(wikis.getWikis()));
  }
  
  private void updateModels(final List<TemplateModel> templates, final List<WikiModel> wikis) {
    updateModel(templates, this.templatesModel);
    updateModel(wikis, this.wikisModel);
  }

  private List<TemplateModel> getTemplateModels(final List<Template> templates) {
    final List<TemplateModel> templateModels = new ArrayList<>(templates.size());
    for (final Template template : templates) {
      templateModels.add(new TemplateModel(template));
    }
    return templateModels;
  }
  
  private List<WikiModel> getWikiModels(final List<Wiki> wikis) {
    final List<WikiModel> wikiModels = new ArrayList<>(wikis.size());
    for (final Wiki wiki : wikis) {
      wikiModels.add(new WikiModel(wiki));
    }
    return wikiModels;
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
  
  public void update(final TemplateModel model, final int index) {
    this.templatesModel.set(index, model);
  }
  
  public void update(final WikiModel model, final int index) {
    this.wikisModel.set(index, model);
  }
}
