package comeon.ui.preferences.templates;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.Charset;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import comeon.model.TemplateKind;
import comeon.ui.UI;
import comeon.ui.preferences.SubPanel;

public final class TemplateSubPanel extends SubPanel<TemplateModel> {
  
  public static final String SELECTED_FILE_PROPERTY = "selectedFile";

  private static final long serialVersionUID = 1L;
  
  private final JTextField nameField;
  
  private final JTextArea descriptionField;
  
  private final JScrollPane descriptionPane;
  
  private final JTextField fileField;
  
  private final JButton fileButton;
  
  private final JComboBox<Charset> charsetField;
  
  private final JComboBox<TemplateKind> kindField;
  
  private final JFileChooser fileChooser;

  public TemplateSubPanel(final Charset[] charsets, final TemplateKind[] kinds) {
    this.nameField = new JTextField();
    this.descriptionField = new JTextArea(3, 0);
    this.descriptionPane = new JScrollPane(descriptionField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    this.fileField = new JTextField();
    this.fileField.setEditable(false);
    this.fileButton = new JButton(UI.BUNDLE.getString("prefs.templates.path.pick"));
    this.charsetField = new JComboBox<>(charsets);
    this.kindField = new JComboBox<TemplateKind>(kinds);
    this.fileChooser = new JFileChooser();
    this.fileChooser.setMultiSelectionEnabled(false);
    this.fileButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            final int option = fileChooser.showOpenDialog(SwingUtilities.getRoot(TemplateSubPanel.this));
            switch (option) {
            case JFileChooser.APPROVE_OPTION:
              final File selectedFile = fileChooser.getSelectedFile();
              TemplateSubPanel.this.firePropertyChange(SELECTED_FILE_PROPERTY, null, selectedFile);
              break;
            default:
              break;
            }
          }
        });
      }
    });
    this.layoutComponents();
  }
  
  JTextField getNameField() {
    return nameField;
  }

  JTextArea getDescriptionField() {
    return descriptionField;
  }

  JTextField getFileField() {
    return fileField;
  }

  JButton getFileButton() {
    return fileButton;
  }

  JComboBox<Charset> getCharsetField() {
    return charsetField;
  }

  JComboBox<TemplateKind> getKindField() {
    return kindField;
  }
  
  JFileChooser getFileChooser() {
    return fileChooser;
  }

  @Override
  protected void doLayoutComponents(final GroupLayout layout) {
    final JLabel[] labels = new JLabel[] {
        new AssociatedLabel("prefs.templates.name", nameField),
        new AssociatedLabel("prefs.templates.description", descriptionField),
        new AssociatedLabel("prefs.templates.path", fileField),
        new AssociatedLabel("prefs.templates.charset", charsetField),
        new AssociatedLabel("prefs.templates.kind", kindField)
    };
    layout.setVerticalGroup(
        layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup().addComponent(labels[0]).addComponent(nameField))
        .addGroup(layout.createParallelGroup().addComponent(labels[1]).addComponent(descriptionPane))
        .addGroup(layout.createParallelGroup().addComponent(labels[2]).addComponent(fileField).addComponent(fileButton))
        .addGroup(layout.createParallelGroup().addComponent(labels[3]).addComponent(charsetField))
        .addGroup(layout.createParallelGroup().addComponent(labels[4]).addComponent(kindField))
    );
    layout.setHorizontalGroup(
        layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup().addComponent(labels[0]).addComponent(labels[1]).addComponent(labels[2]).addComponent(labels[3]).addComponent(labels[4]))
        .addGroup(layout.createParallelGroup().addComponent(nameField).addComponent(descriptionPane).addComponent(fileField).addComponent(charsetField).addComponent(kindField))
        .addGroup(layout.createParallelGroup().addComponent(fileButton))
    );
  }

}
