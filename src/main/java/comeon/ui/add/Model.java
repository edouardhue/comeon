package comeon.ui.add;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

public class Model {
  private final PropertyChangeSupport pcs;
  
  private File[] picturesFiles;
  
  private Boolean useMetadata;
  
  private File metadataFile;
  
  private String pictureExpression;
  
  private String metadataExpression;
  
  public enum Properties {
    PICTURES_FILES,
    USE_METADATA,
    METADATA_FILE,
    PICTURE_EXPRESSION,
    METADATA_EXPRESSION
  }
  
  public Model() {
    this.pcs = new PropertyChangeSupport(this);
    this.picturesFiles = new File[0];
    this.useMetadata = Boolean.FALSE;
    this.metadataFile = null;
    this.pictureExpression = null;
    this.metadataExpression = null;
  }

  public void addPropertyChangeListener(final PropertyChangeListener pcl) {
    this.pcs.addPropertyChangeListener(pcl);
  }
  
  public File[] getPicturesFiles() {
    return picturesFiles;
  }
  
  public void setPicturesFiles(final File[] picturesFiles) {
    final File[] oldPicturesFile = this.picturesFiles;
    this.picturesFiles = picturesFiles;
    pcs.firePropertyChange(Properties.PICTURES_FILES.name(), oldPicturesFile, picturesFiles);
  }
  
  public Boolean getUseMetadata() {
    return useMetadata;
  }
  
  public void setUseMetadata(final Boolean useMetadata) {
    final Boolean oldUseMetadata = this.useMetadata;
    this.useMetadata = useMetadata;
    pcs.firePropertyChange(Properties.USE_METADATA.name(), oldUseMetadata, useMetadata);
  }
  
  public File getMetadataFile() {
    return metadataFile;
  }
  
  public void setMetadataFile(final File metadataFile) {
    final File oldMetadataFile = this.metadataFile;
    this.metadataFile = metadataFile;
    pcs.firePropertyChange(Properties.METADATA_FILE.name(), oldMetadataFile, metadataFile);
  }
  
  public String getPictureExpression() {
    return pictureExpression;
  }
  
  public void setPictureExpression(final String pictureExpression) {
    final String oldPictureExpression = this.pictureExpression;
    this.pictureExpression = pictureExpression;
    pcs.firePropertyChange(Properties.PICTURE_EXPRESSION.name(), oldPictureExpression, pictureExpression);
  }
  
  public String getMetadataExpression() {
    return metadataExpression;
  }
  
  public void setMetadataExpression(final String metadataExpression) {
    final String oldMetadataExpression = this.metadataExpression;
    this.metadataExpression = metadataExpression;
    pcs.firePropertyChange(Properties.METADATA_EXPRESSION.name(), oldMetadataExpression, metadataExpression);
  }
}