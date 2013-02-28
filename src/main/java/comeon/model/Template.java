package comeon.model;

import java.io.File;

public final class Template {
  private final String name;

  private final String description;
  
  private final File file;
  
  private final String templateText;
  
  private final TemplateKind kind;

  public Template(final String name, final String description, final File file, final String templateText, final TemplateKind kind) {
    super();
    this.name = name;
    this.description = description;
    this.file = file;
    this.templateText = templateText;
    this.kind = kind;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public File getFile() {
    return file;
  }

  public String getTemplateText() {
    return templateText;
  }

  public TemplateKind getKind() {
    return kind;
  }

}
