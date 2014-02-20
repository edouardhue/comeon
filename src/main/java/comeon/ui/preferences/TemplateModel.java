package comeon.ui.preferences;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.nio.charset.Charset;

import com.google.common.base.Strings;

import comeon.model.Template;
import comeon.model.TemplateKind;

public final class TemplateModel {
  private final PropertyChangeSupport pcs;

  private String name;

  private String description;

  private File file;

  private Charset charset;

  private TemplateKind kind;

  public enum Properties {
    NAME, DESCRIPTION, FILE, CHARSET, KIND
  }

  public TemplateModel() {
    this.pcs = new PropertyChangeSupport(this);
  }
  
  public TemplateModel(final Template template) {
    this();
    this.name = template.getName();
    this.description = template.getDescription();
    this.file = template.getFile();
    this.charset = template.getCharset();
    this.kind = template.getKind();
  }

  public void addPropertyChangeListener(final PropertyChangeListener pcl) {
    this.pcs.addPropertyChangeListener(pcl);
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    final String oldName = this.name;
    this.name = name;
    pcs.firePropertyChange(Properties.NAME.name(), oldName, name);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    final String oldDescription = this.description;
    this.description = description;
    pcs.firePropertyChange(Properties.DESCRIPTION.name(), oldDescription, description);
  }

  public File getFile() {
    return file;
  }

  public void setFile(final File file) {
    final File oldFile = this.file;
    this.file = file;
    pcs.firePropertyChange(Properties.FILE.name(), oldFile, file);
  }

  public Charset getCharset() {
    return charset;
  }

  public void setCharset(final Charset charset) {
    final Charset oldCharset = this.charset;
    this.charset = charset;
    pcs.firePropertyChange(Properties.CHARSET.name(), oldCharset, charset);
  }

  public TemplateKind getKind() {
    return kind;
  }

  public void setKind(final TemplateKind kind) {
    final TemplateKind oldKind = this.kind;
    this.kind = kind;
    pcs.firePropertyChange(Properties.KIND.name(), oldKind, kind);
  }

  static TemplateModel getPrototype() {
    final TemplateModel prototype = new TemplateModel();
    prototype.name = Strings.repeat("x", 32);
    prototype.description = Strings.repeat("xxxxxx ", 12);
    return prototype;
  }
}
