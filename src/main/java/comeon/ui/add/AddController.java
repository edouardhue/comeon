package comeon.ui.add;

import au.com.bytecode.opencsv.CSVReader;
import comeon.model.Template;
import comeon.templates.Templates;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;


class AddController implements PropertyChangeListener {

    private final DefaultListModel<File> mediaListModel;

    private final DefaultComboBoxModel<String> metadataExpressionModel;

    private final DefaultComboBoxModel<Template> templateModel;

    private AddModel model;

    private AddMediaPanel view;

    public AddController(final Templates templates) {
        this.mediaListModel = new DefaultListModel<>();
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

    public void registerView(final AddMediaPanel view) {
        this.view = view;
    }

    DefaultListModel<File> getMediaListModel() {
        return mediaListModel;
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

    public void setMediaFiles(final File[] mediaFiles) {
        model.setMediaFiles(mediaFiles);
    }

    public void setMediaExpression(final String mediaExpression) {
        model.setMediaExpression(mediaExpression);
    }

    public void setMetadataExpression(final String metadataExpression) {
        model.setMetadataExpression(metadataExpression);
    }

    public String getMediaRegexp() {
        return model.getMediaRegexp();
    }

    public void setMediaRegexp(final String mediaRegexp) {
        model.setMediaRegexp(mediaRegexp);
    }

    public void setMediaSubstitution(final String mediaSubstitution) {
        model.setMediaSubstitution(mediaSubstitution);
    }

    public String getMediaSubstitution() {
        return model.getMediaSubstitution();
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
        } else if (AddModel.Properties.MEDIA_FILES.name().equals(evt.getPropertyName())) {
            mediaListModel.removeAllElements();
            final File[] files = (File[]) evt.getNewValue();
            for (final File file : files) {
                mediaListModel.addElement(file);
            }
        }
    }

    public void updateMetadataFileHeader() {
        final Path location = model.getMetadataFile();
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

    private String[] peekMetadataFileHeader(final Path metadataFile) throws IOException {
        try (final CSVReader reader = new CSVReader(Files.newBufferedReader(metadataFile, model.getCharset()), model.getSeparator(), model.getQuote(), model.getEscape(), model.getSkipLines(),
                model.isStrictQuotes(), model.isIgnoreLeadingWhiteSpace())) {
            return reader.readNext();
        }
    }
}