package comeon.templates;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ComeOn;
import comeon.model.Template;
import comeon.model.TemplateKind;

@Singleton
public final class TemplatesImpl implements Templates {
  private static final Logger LOGGER = LoggerFactory.getLogger(TemplatesImpl.class);

  private final List<Template> templates;

  private final Preferences prefs;

  private boolean loaded;

  @Inject
  private TemplatesImpl() {
    this.templates = new LinkedList<>();
    this.prefs = Preferences.userNodeForPackage(ComeOn.class).node("templates");
    this.loaded = false;
  }

  @Override
  public void readPreferences() throws BackingStoreException {
    final String[] templateNames = prefs.childrenNames();
    for (final String templateName : templateNames) {
      readPref(templateName);
    }
    loaded = true;
  }

  @Override
  public List<Template> getTemplates() {
    if (loaded) {
      return templates;
    } else {
      // TODO i18n
      throw new IllegalStateException("Template preferences not loaded");
    }
  }

  @Override
  public void setTemplates(final List<Template> templates) {
    this.templates.clear();
    this.templates.addAll(templates);
  }

  private void readPref(final String templateName) throws BackingStoreException {
    final Preferences child = prefs.node(templateName);
    final String description = child.get(PreferencesKeys.DESCRIPTION.name(), "");
    try {
      final Charset charset = Charset.forName(child.get(PreferencesKeys.CHARSET.name(), null));
      final File file = new File(child.get(PreferencesKeys.FILE.name(), null));
      final String templateText = Files.toString(file, charset);
      final TemplateKind kind = TemplateKind.valueOf(child.get(PreferencesKeys.KIND.name(), ""));
      final Template template = new Template(templateName, description, file, charset, templateText, kind);
      templates.add(template);
    } catch (final IllegalArgumentException | NullPointerException | IOException e) {
      LOGGER.warn("Got exception, removing template {}", templateName, e);
      child.removeNode();
    }
  }

  @Override
  public void storePreferences() throws BackingStoreException {
    for (final String name : prefs.childrenNames()) {
      prefs.node(name).removeNode();
    }
    for (final Template template : templates) {
      final Preferences node = prefs.node(template.getName());
      node.put(PreferencesKeys.DESCRIPTION.name(), template.getDescription());
      node.put(PreferencesKeys.FILE.name(), template.getFile().getAbsolutePath());
      node.put(PreferencesKeys.CHARSET.name(), template.getCharset().name());
      node.put(PreferencesKeys.KIND.name(), template.getKind().name());
    }
  }

  private enum PreferencesKeys {
    DESCRIPTION, FILE, KIND, CHARSET
  }
}
