package comeon.templates;

import java.util.Collections;
import java.util.List;

import comeon.model.Template;

public final class TemplatesChangedEvent {
  private final List<Template> templates;
  
  public TemplatesChangedEvent(final List<Template> templates) {
    this.templates = Collections.unmodifiableList(templates);
  }
  
  public List<Template> getTemplates() {
    return templates;
  }
}
