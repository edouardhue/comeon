package comeon.ui.add;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import au.com.bytecode.opencsv.CSVReader;


class Controller implements PropertyChangeListener {
  
  private final DefaultListModel<File> picturesListModel;
  
  private final DefaultComboBoxModel<String> metadataExpressionModel;
  
  private Model model;
  
  private FilesPanel view;

  public Controller() {
    this.picturesListModel = new DefaultListModel<>();
    this.metadataExpressionModel = new DefaultComboBoxModel<>();
  }
  
  public void registerModel(final Model model) {
    this.model = model;
    this.model.addPropertyChangeListener(this);
  }
  
  public void registerView(final FilesPanel view) {
    this.view = view;
  }
  
  DefaultListModel<File> getPicturesListModel() {
    return picturesListModel;
  }
  
  public DefaultComboBoxModel<String> getMetadataExpressionModel() {
    return metadataExpressionModel;
  }
  
  public void setUseMetadata(final Boolean useMetadata) {
    model.setUseMetadata(useMetadata);
  }
  
  public void setMetadataFile(final Path metadataFile) {
    model.setMetadataFile(metadataFile);
  }

  public void setPicturesFiles(final File[] picturesFiles) {
    model.setPicturesFiles(picturesFiles);
  }
  
  public void setPictureExpression(final String pictureExpression) {
    model.setPictureExpression(pictureExpression);
  }
  
  public void setMetadataExpression(final String metadataExpression) {
    model.setMetadataExpression(metadataExpression);
  }

  public char getSeparator() {
    return model.getSeparator();
  }
  
  public void setSeparator(final char separator) {
    model.setSeparator(separator);
  }
  
  public char getQuote() {
    return model.getQuote();
  }

  public void setQuote(final char quote) {
    model.setQuote(quote);
  }

  public char getEscape() {
    return model.getEscape();
  }
  
  public void setEscape(final char escape) {
    model.setEscape(escape);
  }
  
  public int getSkipLines() {
    return model.getSkipLines();
  }

  public void setSkipLines(final int skipLines) {
    model.setSkipLines(skipLines);
  }
  
  public boolean isStrictQuotes() {
    return model.isStrictQuotes();
  }
  
  public void setStrictQuotes(final boolean strictQuotes) {
    model.setStrictQuotes(strictQuotes);
  }
  
  public boolean isIgnoreLeadingWhiteSpace() {
    return model.isIgnoreLeadingWhiteSpace();
  }

  public void setIgnoreLeadingWhiteSpace(final boolean ignoreLeadingWhiteSpace) {
    model.setIgnoreLeadingWhiteSpace(ignoreLeadingWhiteSpace);
  }
  
  public Charset getCharset() {
    return model.getCharset();
  }
  
  public void setCharset(final Charset charset) {
    model.setCharset(charset);
  }
  
  @Override
  public void propertyChange(final PropertyChangeEvent evt) {
    if (Model.Properties.USE_METADATA.name().equals(evt.getPropertyName())) {
      if ((Boolean) evt.getNewValue()) {
        view.activateMetadataZone();
      } else {
        view.deactivateMetadataZone();
      }
    } else if (Model.Properties.PICTURES_FILES.name().equals(evt.getPropertyName())) {
      picturesListModel.removeAllElements();
      final File[] files = (File[]) evt.getNewValue();
      for (final File file : files) {
        picturesListModel.addElement(file);
      } 
    } else if (Model.Properties.METADATA_FILE.name().equals(evt.getPropertyName())) {
      final Path location = (Path) evt.getNewValue();
      try {
        final String[] columns = this.peekMetadataFileHeader(location);
        metadataExpressionModel.removeAllElements();
        for (final String column : columns) {
          metadataExpressionModel.addElement(column);
        }
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            view.updateMetadataFileLocation(location.toString());
          }
        });
      } catch (final IOException e) {
        
      }
    }
  }
  
  private String[] peekMetadataFileHeader(final Path metadataFile) throws IOException {
    try (final CSVReader reader = new CSVReader(Files.newBufferedReader(metadataFile, model.getCharset()), model.getSeparator(), model.getQuote(), model.getEscape(), model.getSkipLines(),
        model.isStrictQuotes(), model.isIgnoreLeadingWhiteSpace())) {
      return reader.readNext();
    }
  }
}