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

import comeon.model.Template;
import comeon.templates.Templates;


class AddController implements PropertyChangeListener {
  
  private final DefaultListModel<File> picturesListModel;
  
  private final DefaultComboBoxModel<String> metadataExpressionModel;
  
  private final DefaultComboBoxModel<Template> templateModel;
  
  private AddModel model;
  
  private AddPicturesPanel view;

  public AddController(final Templates templates) {
    this.picturesListModel = new DefaultListModel<>();
    this.metadataExpressionModel = new DefaultComboBoxModel<>();
    this.templateModel = new DefaultComboBoxModel<>();
    for (final Template template : templates.getTemplates()) {
      this.templateModel.addElement(template);
    }
  }
  
  public void registerModel(final AddModel model) {
    this.model = model;
    this.model.addPropertyChangeListener(this);
    if (this.templateModel.getSelectedItem() != null) {
      this.model.setTemplate((Template) this.templateModel.getSelectedItem());
    }
  }
  
  public void registerView(final AddPicturesPanel view) {
    this.view = view;
  }
  
  DefaultListModel<File> getPicturesListModel() {
    return picturesListModel;
  }
  
  public DefaultComboBoxModel<String> getMetadataExpressionModel() {
    return metadataExpressionModel;
  }
 
  public DefaultComboBoxModel<Template> getTemplateModel() {
    return templateModel;
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
  
  public String getPictureRegexp() {
    return model.getPictureRegexp();
  }
  
  public void setPictureRegexp(final String pictureRegexp) {
    model.setPictureRegexp(pictureRegexp);
  }
  
  public void setPictureSubstitution(final String pictureSubstitution) {
    model.setPictureSubstitution(pictureSubstitution);
  }
  
  public String getPictureSubstitution() {
    return model.getPictureSubstitution();
  }

  public void setTemplate(final Template template) {
    model.setTemplate(template);
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
    if (AddModel.Properties.USE_METADATA.name().equals(evt.getPropertyName())) {
      if ((Boolean) evt.getNewValue()) {
        view.activateMetadataZone();
      } else {
        view.deactivateMetadataZone();
      }
    } else if (AddModel.Properties.PICTURES_FILES.name().equals(evt.getPropertyName())) {
      picturesListModel.removeAllElements();
      final File[] files = (File[]) evt.getNewValue();
      for (final File file : files) {
        picturesListModel.addElement(file);
      } 
    } else if (AddModel.Properties.METADATA_FILE.name().equals(evt.getPropertyName())) {
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
        //TODO handle exception
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