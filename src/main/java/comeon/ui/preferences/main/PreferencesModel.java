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
  
  private int activeWikiIndex;

  @Inject
  public PreferencesModel(final Templates templates, final Wikis wikis) {
    this.templatesModel = new DefaultListModel<>();
    this.wikisModel = new DefaultListModel<>();
    this.reload(templates, wikis);
  }
  
  public void reload(final Templates templates, final Wikis wikis) {
    updateModels(buildTemplateModels(templates), buildWikiModels(wikis));
  }
  
  private void updateModels(final List<TemplateModel> templates, final List<WikiModel> wikis) {
    updateModel(templates, this.templatesModel);
    updateModel(wikis, this.wikisModel);
  }

  private List<TemplateModel> buildTemplateModels(final Templates templates) {
    final List<Template> templatesList = templates.getTemplates();
    final List<TemplateModel> templateModels = new ArrayList<>(templatesList.size());
    for (final Template template : templatesList) {
      templateModels.add(new TemplateModel(template));
    }
    return templateModels;
  }
  
  private List<WikiModel> buildWikiModels(final Wikis wikis) {
    final Wiki activeWiki = wikis.getActiveWiki();
    final List<Wiki> wikisList = wikis.getWikis();
    final List<WikiModel> wikiModels = new ArrayList<>(wikisList.size());
    for (final Wiki wiki : wikisList) {
      final boolean active = wiki.equals(activeWiki);
      wikiModels.add(new WikiModel(wiki, active));
      if (active) {
        this.activeWikiIndex = wikiModels.size() - 1;
      }
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
  
  public int getActiveWikiIndex() {
    return activeWikiIndex;
  }
  
  public void setActiveWiki(final int index) {
    final WikiModel previousActiveWiki = wikisModel.get(activeWikiIndex);
    previousActiveWiki.setActive(Boolean.FALSE);
    // Need to replace item by itself to trigger model change events...
    wikisModel.set(activeWikiIndex, previousActiveWiki);
    final WikiModel newActiveWiki = wikisModel.get(index);
    newActiveWiki.setActive(Boolean.TRUE);
    // Here again
    wikisModel.set(index, newActiveWiki);
    this.activeWikiIndex = index;
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
