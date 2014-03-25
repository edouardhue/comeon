package comeon.ui.preferences.main;

import java.io.IOException;
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
    updateModels(buildTemplateModels(templates.getTemplates()), buildWikiModels(wikis.getWikis()));
  }
  
  private void updateModels(final List<TemplateModel> templates, final List<WikiModel> wikis) {
    updateModel(templates, this.templatesModel);
    updateModel(wikis, this.wikisModel);
  }

  private List<TemplateModel> buildTemplateModels(final List<Template> templates) {
    final List<TemplateModel> templateModels = new ArrayList<>(templates.size());
    for (final Template template : templates) {
      templateModels.add(new TemplateModel(template));
    }
    return templateModels;
  }
  
  private List<WikiModel> buildWikiModels(final List<Wiki> wikis) {
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
  
  public DefaultListModel<TemplateModel> getTemplateModels() {
    return templatesModel;
  }
  
  public List<Template> getTemplates() throws IOException {
    final List<Template> templates = new ArrayList<>(templatesModel.getSize());
    for (int i = 0; i < templatesModel.getSize(); i++) {
      final TemplateModel model = templatesModel.get(i);
      templates.add(model.asTemplate());
    }
    return templates;
  }
  
  public DefaultListModel<WikiModel> getWikiModels() {
    return wikisModel;
  }
  
  public List<Wiki> getWikis() {
    final List<Wiki> wikis = new ArrayList<>(wikisModel.getSize());
    for (int i = 0; i < wikisModel.getSize(); i++) {
      final WikiModel model = wikisModel.get(i);
      wikis.add(model.asWiki());
    }
    return wikis;
  }
  
  public void update(final TemplateModel model, final int index) {
    this.templatesModel.set(index, model);
  }
  
  public void update(final WikiModel model, final int index) {
    this.wikisModel.set(index, model);
  }
}
