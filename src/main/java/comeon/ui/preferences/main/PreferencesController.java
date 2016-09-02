package comeon.ui.preferences.main;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.model.Template;
import comeon.model.Wiki;
import comeon.templates.Templates;
import comeon.ui.preferences.templates.TemplateModel;
import comeon.ui.preferences.wikis.WikiModel;
import comeon.wikis.Wikis;

import javax.swing.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.BackingStoreException;

@Singleton
public final class PreferencesController {

    private final Templates templates;

    private final Wikis wikis;

    private final PreferencesModel model;

    @Inject
    public PreferencesController(final Templates templates, final Wikis wikis, final PreferencesModel model) {
        this.templates = templates;
        this.wikis = wikis;
        this.model = model;
    }

    public void reload() {
        this.model.reload(templates, wikis);
    }

    public void add(final TemplateModel model) {
        this.model.getTemplateModels().addElement(model);
    }

    public void add(final WikiModel model) {
        this.model.getWikiModels().addElement(model);
    }

    public void update(final TemplateModel model, final int index) {
        this.model.update(model, index);
    }

    public void update(final WikiModel model, final int index) {
        this.model.update(model, index);
    }

    public void removeTemplate(final int index) {
        model.getTemplateModels().removeElementAt(index);
    }

    public void removeWiki(final int index) {
        model.getWikiModels().removeElementAt(index);
        if (index < model.getActiveWikiIndex()) {
            model.setActiveWiki(model.getActiveWikiIndex() - 1);
        } else if (index == model.getActiveWikiIndex()) {
            model.setActiveWiki(0);
        }
    }

    public void setActiveWiki(final int index) {
        model.setActiveWiki(index);
    }

    public ListModel<TemplateModel> getTemplates() {
        return model.getTemplateModels();
    }

    public ListModel<WikiModel> getWikis() {
        return model.getWikiModels();
    }

    public void persist() throws PreferencesSavingException {
        final List<Exception> exceptions = new LinkedList<>();
        try {
            persistTemplates(exceptions);
            persistWikis(exceptions);
        } catch (final IOException e) {
            exceptions.add(e);
        }
        if (!exceptions.isEmpty()) {
            throw new PreferencesSavingException(exceptions);
        }
    }

    private void persistWikis(final List<Exception> exceptions) {
        final int activeWikiIndex = model.getActiveWikiIndex();
        try {
            final List<Wiki> newWikis = model.getWikis();
            wikis.setWikis(newWikis);
            wikis.setActiveWiki(newWikis.get(activeWikiIndex));
            wikis.save();
        } catch (final BackingStoreException e) {
            exceptions.add(e);
        }
    }

    private void persistTemplates(final List<Exception> exceptions) throws IOException {
        try {
            final List<Template> newTemplates = model.getTemplates();
            templates.setTemplates(newTemplates);
            templates.save();
        } catch (final BackingStoreException e) {
            exceptions.add(e);
        }
    }

}
