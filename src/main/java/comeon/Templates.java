package comeon;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import comeon.model.Template;
import comeon.model.TemplateKind;

final class Templates {
  private static final Logger LOGGER = LoggerFactory.getLogger(Templates.class);
  
  private final List<Template> templates;
  
  private final Preferences prefs;
  
  Templates() throws BackingStoreException {
    this.prefs = Preferences.userNodeForPackage(Templates.class).node("templates");
    final String[] templateNames = prefs.childrenNames();
    this.templates = new ArrayList<>(templateNames.length);
    for (final String templateName : templateNames) {
      readPref(templateName);
    }
  }
  
  List<Template> getTemplates() {
    return templates;
  }

  private void readPref(final String templateName) throws BackingStoreException {
    final Preferences child = prefs.node(templateName);
    final String description = child.get(PreferencesKeys.DESCRIPTION.name(), "");
    try {
      final Charset charset = Charset.forName(child.get(PreferencesKeys.CHARSET.name(), null));
      final File file = new File(child.get(PreferencesKeys.FILE.name(), null));
      final String templateText = Files.toString(file, charset);
      final TemplateKind kind = TemplateKind.valueOf(child.get(PreferencesKeys.KIND.name(), ""));
      final Template template = new Template(templateName, description, file, templateText, kind);
      templates.add(template);
    } catch (final IllegalArgumentException | NullPointerException | IOException e) {
      LOGGER.warn("Got exception, removing template {}", templateName, e);
      child.removeNode();
    }
  }
  
  private enum PreferencesKeys {
    DESCRIPTION,
    FILE,
    KIND,
    CHARSET
  }
}
