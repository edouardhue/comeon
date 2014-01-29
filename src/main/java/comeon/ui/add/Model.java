package comeon.ui.add;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import au.com.bytecode.opencsv.CSVParser;

public class Model {
  private final PropertyChangeSupport pcs;

  private File[] picturesFiles;

  private Boolean useMetadata;

  private File metadataFile;

  private String pictureExpression;

  private String metadataExpression;
  
  private final CSVSettings csvSettings;

  public enum Properties {
    PICTURES_FILES, USE_METADATA, METADATA_FILE, PICTURE_EXPRESSION, METADATA_EXPRESSION,
    CSV_SEPARATOR, CSV_QUOTE, CSV_ESCAPE, CSV_SKIP_LINES, CSV_STRICT_QUOTES, CSV_IGNORE_LEADING_WHITESPACE
  }

  public Model() {
    this.pcs = new PropertyChangeSupport(this);
    this.picturesFiles = new File[0];
    this.useMetadata = Boolean.FALSE;
    this.metadataFile = null;
    this.pictureExpression = null;
    this.metadataExpression = null;
    this.csvSettings = new CSVSettings();
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

  public class CSVSettings {
    private char separator;

    private char quote;

    private char escape;

    private int skipLines;

    private boolean strictQuotes;

    private boolean ignoreLeadingWhiteSpace;

    public CSVSettings() {
      this.separator = CSVParser.DEFAULT_SEPARATOR;
      this.quote = CSVParser.DEFAULT_QUOTE_CHARACTER;
      this.escape = CSVParser.DEFAULT_ESCAPE_CHARACTER;
      this.skipLines = 0;
      this.strictQuotes = CSVParser.DEFAULT_STRICT_QUOTES;
      this.ignoreLeadingWhiteSpace = CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
    }
  }
}