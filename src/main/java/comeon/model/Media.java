package comeon.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.Map;

public final class Media {
  private final PropertyChangeSupport pcs;
  
  private final File file;

  private final String fileName;

  private final Template template;

  private String templateText;

  private String renderedTemplate;

  private final Map<String, Object> metadata;

  private final byte[] thumbnail;
  
  private State state;

  public Media(final File file, final String fileName, final Template template, final Map<String, Object> metadata,
      final byte[] thumbnail) {
    super();
    this.pcs = new PropertyChangeSupport(this);
    this.file = file;
    this.fileName = fileName;
    this.template = template;
    this.templateText = template.getTemplateText();
    this.metadata = metadata;
    this.thumbnail = thumbnail;
    this.state = State.ToBeUploaded;
  }

  public void renderTemplate(final User user) {
    this.setRenderedTemplate(template.getKind().render(template, templateText, user, this));
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
    final String oldTemplateText = this.templateText;
    this.templateText = templateText;
    pcs.firePropertyChange("templateText", oldTemplateText, templateText);
  }

  public String getRenderedTemplate() {
    return renderedTemplate;
  }

  public void setRenderedTemplate(final String renderedTemplate) {
    final String oldRenderedTemplate = this.renderedTemplate;
    this.renderedTemplate = renderedTemplate;
    pcs.firePropertyChange("renderedTemplate", oldRenderedTemplate, renderedTemplate);
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public byte[] getThumbnail() {
    return thumbnail;
  }

  public String getFileName() {
    return fileName;
  }
  
  public State getState() {
    return state;
  }
  
  public void setState(final State state) {
    final State oldState = this.state;
    this.state = state;
    pcs.firePropertyChange("state", oldState, state);
  }
  
  public void addPropertyChangeListener(final PropertyChangeListener pcl) {
    this.pcs.addPropertyChangeListener(pcl);
  }
  
  public void removePropertyChangeLister(final PropertyChangeListener pcl) {
    this.pcs.removePropertyChangeListener(pcl);
  }
  
  @Override
  public boolean equals(final Object obj) {
    final boolean isEqual;
    if (obj == null) {
      isEqual = false;
    } else if (obj instanceof Media) {
      final Media o = (Media) obj;
      isEqual = this.file.equals(o.file);
    } else {
      isEqual = false;
    }
    return isEqual;
  }
  
  @Override
  public int hashCode() {
    return this.file.hashCode();
  }
  
  public enum State {
    ToBeUploaded,
    UploadedSuccessfully,
    FailedUpload
  }
}
