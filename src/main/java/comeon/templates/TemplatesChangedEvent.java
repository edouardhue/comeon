package comeon.templates;

import comeon.model.Template;

import java.util.Collections;
import java.util.List;

public final class TemplatesChangedEvent {
    private final List<Template> templates;

    public TemplatesChangedEvent(final List<Template> templates) {
        this.templates = Collections.unmodifiableList(templates);
    }

    public List<Template> getTemplates() {
        return templates;
    }
}
