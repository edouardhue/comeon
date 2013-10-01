package comeon.ui.preferences;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import layout.SpringUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import comeon.model.Template;
import comeon.model.TemplateKind;
import comeon.ui.UI;

final class TemplatePanel extends JOptionPane {

  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(TemplatePanel.class);

  private final JDialog dialog;
  
  private final JPanel fieldsPanel;
  
  private final JTextField nameField;
  
  private final JTextArea descriptionField;
  
  private final JTextField pathField;
  
  private final JComboBox<TemplateKind> kindField;
  
  private final JComboBox<Charset> charsetField;
  
  private final JFileChooser fileChooser;
  
  private File templateFile;
  
  private String templateText;

  public TemplatePanel(final Template template) {
    super(null, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
    this.fileChooser = new JFileChooser();
    this.fieldsPanel = new JPanel(new SpringLayout());
    this.nameField = new JTextField(template == null ? "" : template.getName(), 20);
    this.descriptionField = new JTextArea(template == null ? "" : template.getDescription(), 3, 20);
    if (template != null) {
      this.templateFile = template.getFile();
      this.templateText = template.getTemplateText();
    }
    this.pathField = new JTextField(templateFile == null ? "" : templateFile.getAbsolutePath(), 20);
    this.kindField = new JComboBox<>(TemplateKind.values());
    this.charsetField = new JComboBox<>(new Charset[] {
        Charsets.UTF_8,
        Charsets.ISO_8859_1
    });
    this.build();
    this.setMessage(this.fieldsPanel);
    this.dialog = this.createDialog(JOptionPane.getRootFrame(), UI.BUNDLE.getString("action.preferences.title"));
  }
  
  private void build() {
    final JLabel nameLabel = new JLabel(UI.BUNDLE.getString("prefs.templates.name"), SwingConstants.TRAILING);
    nameLabel.setLabelFor(nameField);
    fieldsPanel.add(nameLabel);
    fieldsPanel.add(nameField);

    final JLabel descriptionLabel = new JLabel(UI.BUNDLE.getString("prefs.templates.description"), SwingConstants.TRAILING);
    descriptionLabel.setLabelFor(descriptionField);
    fieldsPanel.add(descriptionLabel);
    descriptionField.setLineWrap(true);
    fieldsPanel.add(new JScrollPane(descriptionField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

    final JLabel charsetLabel = new JLabel(UI.BUNDLE.getString("prefs.templates.charset"), SwingConstants.TRAILING);
    charsetLabel.setLabelFor(charsetField);
    fieldsPanel.add(charsetLabel);
    fieldsPanel.add(charsetField);

    final JLabel pathLabel = new JLabel(UI.BUNDLE.getString("prefs.templates.path"), SwingConstants.TRAILING);
    pathLabel.setLabelFor(pathField);
    fieldsPanel.add(pathLabel);
    this.pathField.setEditable(false);
    final JPanel pathPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
    pathPanel.add(pathField);
    pathPanel.add(new JButton(new ChooseFileAction()));
    fieldsPanel.add(pathPanel);

    final JLabel kindLabel = new JLabel(UI.BUNDLE.getString("prefs.templates.kind"), SwingConstants.TRAILING);
    kindLabel.setLabelFor(kindField);
    fieldsPanel.add(kindLabel);
    fieldsPanel.add(kindField);
    
    SpringUtilities.makeCompactGrid(fieldsPanel, 5, 2, 6, 6, 6, 6);
  }
  
  private final class ChooseFileAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public ChooseFileAction() {
      super("…");
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {

      final int chooserReturnVal = fileChooser.showOpenDialog(TemplatePanel.this);
      if (JFileChooser.APPROVE_OPTION == chooserReturnVal) {
        final File selectedFile = fileChooser.getSelectedFile();
        try {
          templateText = Files.toString(selectedFile, (Charset) charsetField.getSelectedItem());
          templateFile = selectedFile;
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              pathField.setText(templateFile.getAbsolutePath());
            }
          });
        } catch (final IOException ex) {
          LOGGER.error(UI.BUNDLE.getString("prefs.error.save"), ex);
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              JOptionPane.showMessageDialog(
                  TemplatePanel.this,
                  new StringBuffer(UI.BUNDLE.getString("prefs.templates.error.read")).append('\n').append(ex.getLocalizedMessage()),
                  UI.BUNDLE.getString("error.generic.title"),
                  JOptionPane.ERROR_MESSAGE);
            }
          });
        }
      }
    }
  }
  
  public int showDialog() {
    this.dialog.setVisible(true);
    return ((Integer) this.getValue()).intValue();
  }
  
  Template getTemplate() {
    final Charset charset = (Charset) charsetField.getSelectedItem();
    return new Template(nameField.getText(), descriptionField.getText(), templateFile, charset, templateText, (TemplateKind) kindField.getSelectedItem());
  }
}
