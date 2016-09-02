package comeon.templates;

import com.google.common.base.Charsets;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ComeOn;
import comeon.model.Template;
import comeon.model.TemplateKind;
import comeon.templates.velocity.VelocityTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

@Singleton
public final class TemplatesImpl implements Templates {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplatesImpl.class);

    private final ArrayList<Template> templates;

    private final Preferences prefs;

    private final Map<String, TemplateKind> templateKinds;

    private final EventBus bus;

    @Inject
    private TemplatesImpl(final Map<String, TemplateKind> templateKinds, final EventBus bus) {
        this.templates = new ArrayList<>(0);
        this.prefs = Preferences.userNodeForPackage(ComeOn.class).node("templates");
        this.templateKinds = templateKinds;
        this.bus = bus;
    }

    @Override
    public void loadPreferences() throws BackingStoreException {
        final String[] templateNames = prefs.childrenNames();
        this.templates.ensureCapacity(templateNames.length);
        for (final String templateName : templateNames) {
            readNode(templateName);
        }
    }

    @Override
    public List<Template> getTemplates() {
        return new ArrayList<>(templates);
    }

    @Override
    public void setTemplates(final List<Template> templates) {
        this.templates.clear();
        this.templates.ensureCapacity(templates.size());
        this.templates.addAll(templates);
        this.bus.post(new TemplatesChangedEvent(this.getTemplates()));
    }

    @Override
    public List<TemplateKind> getTemplateKinds() {
        // TODO use a predictable sort
        return new ArrayList<>(templateKinds.values());
    }

    @Override
    public Charset[] getSupportedCharsets() {
        return new Charset[]{Charsets.UTF_8, Charsets.ISO_8859_1};
    }

    @Override
    public void save() throws BackingStoreException {
        for (final String name : prefs.childrenNames()) {
            prefs.node(name).removeNode();
        }
        for (final Template template : templates) {
            final Preferences node = prefs.node(template.getName());
            node.put(PreferencesKeys.DESCRIPTION.name(), template.getDescription());
            node.put(PreferencesKeys.FILE.name(), template.getFile().toString());
            node.put(PreferencesKeys.CHARSET.name(), template.getCharset().name());
            node.put(PreferencesKeys.KIND.name(), template.getKind().getClass().getSimpleName());
        }
    }

    private void readNode(final String templateName) throws BackingStoreException {
        final Preferences node = prefs.node(templateName);
        final String description = node.get(PreferencesKeys.DESCRIPTION.name(), "");
        try {
            final Charset charset = Charset.forName(node.get(PreferencesKeys.CHARSET.name(), null));
            final Path file = Paths.get(node.get(PreferencesKeys.FILE.name(), null));
            final String templateText = Template.read(file, charset);
            final TemplateKind kind = templateKinds.get(node.get(PreferencesKeys.KIND.name(), VelocityTemplate.class.getSimpleName()));
            final Template template = new Template(templateName, description, file, charset, templateText, kind);
            templates.add(template);
        } catch (final IllegalArgumentException | NullPointerException | IOException e) {
            LOGGER.warn("Got exception, removing template {}", templateName, e);
            node.removeNode();
        }
    }

    private enum PreferencesKeys {
        DESCRIPTION, FILE, KIND, CHARSET
    }
}
