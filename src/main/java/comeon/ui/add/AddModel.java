package comeon.ui.add;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;

import au.com.bytecode.opencsv.CSVParser;

import com.google.common.base.Charsets;

import comeon.core.extmetadata.CsvMetadataSource;
import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.core.extmetadata.KeyTransformer;
import comeon.core.extmetadata.NullMetadataSource;
import comeon.model.Template;

public class AddModel {
  private final PropertyChangeSupport pcs;

  private File[] picturesFiles;

  private Template template;
  
  private Boolean useMetadata;

  private Path metadataFile;

  private String pictureExpression;

  private String metadataExpression;
  
  private String pictureRegexp;
  
  private String pictureSubstitution;
  
  private final CSVSettings csvSettings;

  public enum Properties {
    PICTURES_FILES, TEMPLATE, USE_METADATA, METADATA_FILE, PICTURE_EXPRESSION, METADATA_EXPRESSION, PICTURE_REGEXP, PICTURE_SUBSTITUTION,
    CSV_SEPARATOR, CSV_QUOTE, CSV_ESCAPE, CSV_SKIP_LINES, CSV_STRICT_QUOTES, CSV_IGNORE_LEADING_WHITESPACE, CSV_CHARSET
  }

  public AddModel() {
    this.pcs = new PropertyChangeSupport(this);
    this.picturesFiles = new File[0];
    this.template = null;
    this.useMetadata = Boolean.FALSE;
    this.metadataFile = null;
    this.pictureExpression = null;
    this.metadataExpression = null;
    this.pictureRegexp = ".*";
    this.pictureSubstitution = "${0}";
    this.csvSettings = new CSVSettings();
  }

  public void addPropertyChangeListener(final PropertyChangeListener pcl) {
    this.pcs.addPropertyChangeListener(pcl);
  }
  
  public  ExternalMetadataSource<?> getExternalMetadataSource() {
    final ExternalMetadataSource<?> externalMetadataSource;
    if (useMetadata && metadataFile != null) {
      final KeyTransformer keyTransformer = new KeyTransformer(pictureRegexp, pictureSubstitution);
      externalMetadataSource = new CsvMetadataSource(pictureExpression, metadataExpression, metadataFile, csvSettings.separator,
          csvSettings.quote, csvSettings.escape, csvSettings.skipLines, csvSettings.strictQuotes, csvSettings.ignoreLeadingWhiteSpace, csvSettings.charset, keyTransformer);
    } else {
      externalMetadataSource = new NullMetadataSource();
    }
    return externalMetadataSource;
  }

  public File[] getPicturesFiles() {
    return picturesFiles;
  }

  public void setPicturesFiles(final File[] picturesFiles) {
    final File[] oldPicturesFile = this.picturesFiles;
    this.picturesFiles = picturesFiles;
    pcs.firePropertyChange(Properties.PICTURES_FILES.name(), oldPicturesFile, picturesFiles);
  }
  
  public Template getTemplate() {
    return template;
  }
  
  public void setTemplate(final Template template) {
    final Template oldTemplate = this.template;
    this.template = template;
    pcs.firePropertyChange(Properties.TEMPLATE.name(), oldTemplate, template);
  }

  public Boolean getUseMetadata() {
    return useMetadata;
  }

  public void setUseMetadata(final Boolean useMetadata) {
    final Boolean oldUseMetadata = this.useMetadata;
    this.useMetadata = useMetadata;
    pcs.firePropertyChange(Properties.USE_METADATA.name(), oldUseMetadata, useMetadata);
  }

  public Path getMetadataFile() {
    return metadataFile;
  }

  public void setMetadataFile(final Path metadataFile) {
    final Path oldMetadataFile = this.metadataFile;
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
  
  public String getPictureRegexp() {
    return pictureRegexp;
  }
  
  public void setPictureRegexp(final String pictureRegexp) {
    final String oldPictureRegexp = this.pictureRegexp;
    this.pictureRegexp = pictureRegexp;
    pcs.firePropertyChange(Properties.PICTURE_REGEXP.name(), oldPictureRegexp, pictureRegexp);
  }
  
  public String getPictureSubstitution() {
    return pictureSubstitution;
  }
  
  public void setPictureSubstitution(final String pictureSubstitution) {
    final String oldPictureSubstitution = this.pictureSubstitution;
    this.pictureSubstitution = pictureSubstitution;
    pcs.firePropertyChange(Properties.PICTURE_SUBSTITUTION.name(), oldPictureSubstitution, pictureSubstitution);
  }

  public char getSeparator() {
    return csvSettings.separator;
  }

  public void setSeparator(final char separator) {
    final char oldSeparator = csvSettings.separator;
    csvSettings.separator = separator;
    pcs.firePropertyChange(Properties.CSV_SEPARATOR.name(), oldSeparator, separator);
  }

  public char getQuote() {
    return csvSettings.quote;
  }

  public void setQuote(final char quote) {
    final char oldQuote = csvSettings.quote;
    csvSettings.quote = quote;
    pcs.firePropertyChange(Properties.CSV_QUOTE.name(), oldQuote, quote);
  }

  public char getEscape() {
    return csvSettings.escape;
  }

  public void setEscape(final char escape) {
    final char oldEscape = csvSettings.escape;
    csvSettings.escape = escape;
    pcs.firePropertyChange(Properties.CSV_ESCAPE.name(), oldEscape, escape);
  }

  public int getSkipLines() {
    return csvSettings.skipLines;
  }

  public void setSkipLines(final int skipLines) {
    final int oldSkipLines = csvSettings.skipLines;
    csvSettings.skipLines = skipLines;
    pcs.firePropertyChange(Properties.CSV_SKIP_LINES.name(), oldSkipLines, skipLines);
  }

  public boolean isStrictQuotes() {
    return csvSettings.strictQuotes;
  }

  public void setStrictQuotes(final boolean strictQuotes) {
    final boolean oldStrictQuotes = csvSettings.strictQuotes;
    csvSettings.strictQuotes = strictQuotes;
    pcs.firePropertyChange(Properties.CSV_STRICT_QUOTES.name(), oldStrictQuotes, strictQuotes);
  }

  public boolean isIgnoreLeadingWhiteSpace() {
    return csvSettings.ignoreLeadingWhiteSpace;
  }

  public void setIgnoreLeadingWhiteSpace(final boolean ignoreLeadingWhiteSpace) {
    final boolean oldIgnoreLeadingWhiteSpace = csvSettings.ignoreLeadingWhiteSpace;
    csvSettings.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
    pcs.firePropertyChange(Properties.CSV_IGNORE_LEADING_WHITESPACE.name(), oldIgnoreLeadingWhiteSpace, ignoreLeadingWhiteSpace);
  }
  
  public Charset getCharset() {
    return csvSettings.charset;
  }
  
  public void setCharset(final Charset charset) {
    final Charset oldCharset = csvSettings.charset;
    csvSettings.charset = charset;
    pcs.firePropertyChange(Properties.CSV_CHARSET.name(), oldCharset, charset);
  }

  public class CSVSettings {
    private char separator;

    private char quote;

    private char escape;

    private int skipLines;

    private boolean strictQuotes;

    private boolean ignoreLeadingWhiteSpace;
    
    private Charset charset;

    public CSVSettings() {
      this.separator = CSVParser.DEFAULT_SEPARATOR;
      this.quote = CSVParser.DEFAULT_QUOTE_CHARACTER;
      this.escape = CSVParser.DEFAULT_ESCAPE_CHARACTER;
      this.skipLines = 0;
      this.strictQuotes = CSVParser.DEFAULT_STRICT_QUOTES;
      this.ignoreLeadingWhiteSpace = CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
      this.charset = Charsets.UTF_8;
    }
  }
}