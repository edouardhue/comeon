package comeon.model;

import java.io.File;
import java.util.Map;

public final class Picture {
  private final File file;

  private final String fileName;

  private final Template template;
  
  private String templateText;

  private String renderedTemplate;

  private final Map<String, Object> metadata;

  private final byte[] thumbnail;

  public Picture(final File file, final String fileName, final Template template, final Map<String, Object> metadata, final byte[] thumbnail) {
    super();
    this.file = file;
    this.fileName = fileName;
    this.template = template;
    this.templateText = template.getTemplateText();
    this.metadata = metadata;
    this.thumbnail = thumbnail;
  }
  
  public void renderTemplate(final User user) {
    this.renderedTemplate = template.getKind().render(templateText, user, this);
  }

  public File getFile() {
    return file;
  }

  public Template getTemplate() {
    return template;
  }
  
  public String getTemplateText() {
    return templateText;
  }
  
  public void setTemplateText(final String templateText) {
    this.templateText = templateText;
  }

  public String getRenderedTemplate() {
    return renderedTemplate;
  }
  
  public void setRenderedTemplate(final String renderedTemplate) {
    this.renderedTemplate = renderedTemplate;
  }
  
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public  byte[] getThumbnail() {
    return thumbnail;
  }

  public String getFileName() {
    return fileName;
  }
}
