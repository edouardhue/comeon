package comeon.ui.add;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.text.ParseException;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MaskFormatter;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import comeon.model.Template;
import comeon.ui.UI;
import comeon.ui.actions.BaseAction;

class AddPicturesPanel extends JPanel {
  private static final int MEDIUM_PROTOTYPE_LENGTH = 20;

  private static final String PROTOTYPE_CHAR = "x";

  private static final int LONG_PROTOTYPE_LENGTH = 30;

  private static final long serialVersionUID = 1L;

  private final JFileChooser picturesFilesChooser;
  
  private final JComboBox<Template> templates;

  private final JCheckBox metatadataCheckbox;
  
  private final JFileChooser metadataFileChooser;
  
  private final JLabel metadataFileLabel;
  
  private final JTextField metadataFileLocation;
  
  private final JButton pickMetadataFileButton;
  
  private final JLabel metadataMatchLabel;
  
  private final JTextField pictureExpression;
  
  private final JLabel metadataMatchSymbol;
  
  private final JComboBox<String> metadataExpression;
  
  private final AddController controller;
  
  public AddPicturesPanel(final AddController controller) {
    this.controller = controller;
    
    final GroupLayout layout = new GroupLayout(this);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    this.setLayout(layout);
    
    this.picturesFilesChooser = new JFileChooser();
    picturesFilesChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    picturesFilesChooser.setMultiSelectionEnabled(true);
    picturesFilesChooser.setFileFilter(new FileNameExtensionFilter(UI.BUNDLE.getString("action.addpictures.filter"), "jpg", "jpeg"));
    this.metadataFileChooser = new JFileChooser();
    metadataFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    metadataFileChooser.setMultiSelectionEnabled(false);
    metadataFileChooser.setFileFilter(new FileNameExtensionFilter(UI.BUNDLE.getString("addpictures.metadata.filter"), "csv"));
    metadataFileChooser.setAccessory(new CSVAccessoryPanel());

    final JLabel filesListLabel = new JLabel(UI.BUNDLE.getString("addpictures.pictures.label"));

    final JList<File> filesList = new JList<>(controller.getPicturesListModel());
    filesList.setPrototypeCellValue(new File(Strings.repeat(PROTOTYPE_CHAR, LONG_PROTOTYPE_LENGTH)));
    final JScrollPane filesListPanel = new JScrollPane(filesList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    final JButton pickPicturesFilesButton = new JButton(new PickPicturesFilesAction(controller));

    final JLabel templatesLabel = new JLabel(UI.BUNDLE.getString("addpictures.template.label"));
    this.templates = new JComboBox<Template>(controller.getTemplateModel());
    this.templates.setRenderer(new DefaultListCellRenderer() {
      private static final long serialVersionUID = 1L;
      
      @Override
      public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected,
          final boolean cellHasFocus) {
        final JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        final Template template = (Template) value;
        label.setText(template.getName());
        return label;
      }
    });
    this.templates.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        templateChanged(e);
      }
    });
    
    final UseExternalMetadataCheckboxHandler checkboxHandler = new UseExternalMetadataCheckboxHandler(controller);
    this.metatadataCheckbox = new JCheckBox(checkboxHandler);
    metatadataCheckbox.addItemListener(checkboxHandler);

    this.metadataFileLabel = new JLabel(UI.BUNDLE.getString("addpictures.metadata.label"));
    this.metadataFileLocation = new JTextField(LONG_PROTOTYPE_LENGTH);
    this.metadataFileLocation.setEditable(false);
    
    this.pickMetadataFileButton = new JButton(new PickMetadataFileAction());
    
    this.metadataMatchLabel = new JLabel(UI.BUNDLE.getString("addpictures.metadata.match.label"));
    
    this.metadataExpression = new JComboBox<>(controller.getMetadataExpressionModel());
    this.metadataExpression.setPrototypeDisplayValue(Strings.repeat(PROTOTYPE_CHAR, MEDIUM_PROTOTYPE_LENGTH));
    this.metadataExpression.setToolTipText(UI.BUNDLE.getString("addpictures.metadata.match.metadata"));
    this.metadataExpression.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        metadataExpressionChanged(e);
      }
    });
    
    this.pictureExpression = new JTextField(MEDIUM_PROTOTYPE_LENGTH);
    this.pictureExpression.setToolTipText(UI.BUNDLE.getString("addpictures.metadata.match.picture"));
    this.pictureExpression.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void removeUpdate(final DocumentEvent e) {
        pictureExpressionChanged(e);
      }
      
      @Override
      public void insertUpdate(final DocumentEvent e) {
        pictureExpressionChanged(e);
      }
      
      @Override
      public void changedUpdate(final DocumentEvent e) {
        pictureExpressionChanged(e);
      }
    });
    
    this.metadataMatchSymbol = new JLabel(UI.BUNDLE.getString("addpictures.metadata.match.symbol"));

    layout.setVerticalGroup(
        layout.createSequentialGroup()
        .addComponent(filesListLabel)
        .addGroup(
            layout.createParallelGroup(Alignment.BASELINE)
            .addComponent(filesListPanel)
            .addComponent(pickPicturesFilesButton)
        )
        .addGroup(
            layout.createParallelGroup(Alignment.BASELINE)
            .addComponent(templatesLabel)
            .addComponent(templates)
        )
        .addComponent(metatadataCheckbox)
        .addGroup(
            layout.createParallelGroup(Alignment.BASELINE)
            .addComponent(metadataFileLabel)
            .addComponent(metadataFileLocation)
            .addComponent(pickMetadataFileButton)
        )
        .addGroup(
            layout.createParallelGroup(Alignment.BASELINE)
            .addComponent(metadataMatchLabel)
            .addComponent(pictureExpression)
            .addComponent(metadataMatchSymbol)
            .addComponent(metadataExpression)
        )
    );
    layout.setHorizontalGroup(
        layout.createSequentialGroup()
        .addGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(filesListLabel)
            .addComponent(filesListPanel)
            .addComponent(metatadataCheckbox)
            .addGroup(
                layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup()
                    .addComponent(templatesLabel)
                    .addComponent(metadataFileLabel)
                    .addComponent(metadataMatchLabel)
                )
                .addGroup(
                    layout.createParallelGroup()
                    .addComponent(metadataFileLocation)
                    .addComponent(templates)
                    .addGroup(
                        layout.createSequentialGroup()
                        .addComponent(metadataExpression)
                        .addComponent(metadataMatchSymbol)
                        .addComponent(pictureExpression)
                    )
                )
            )
        )
        .addGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(pickPicturesFilesButton)
            .addComponent(pickMetadataFileButton)
        )
    );
    
    this.deactivateMetadataZone();
  }
  
  private void templateChanged(final ActionEvent e) {
    final Template selectedTemplate = (Template) this.templates.getModel().getSelectedItem();
    controller.setTemplate(selectedTemplate);
  }
  
  private void pictureExpressionChanged(final DocumentEvent e) {
    final Document document = e.getDocument();
    try {
      controller.setPictureExpression(document.getText(0, document.getLength()));
    } catch (final BadLocationException e1) {
    }
  }
  
  private void metadataExpressionChanged(final ActionEvent e) {
    final String selectedValue = (String) this.metadataExpression.getModel().getSelectedItem();
    controller.setMetadataExpression(selectedValue);
  }
  
  private void toggleMetadataZone(final boolean state) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        pickMetadataFileButton.setEnabled(state);
        metadataFileLabel.setEnabled(state);
        metadataFileLocation.setEnabled(state);
        metadataMatchLabel.setEnabled(state);
        metadataExpression.setEnabled(state);
        metadataMatchSymbol.setEnabled(state);
        pictureExpression.setEnabled(state);
      }
    });
  }
  
  public void activateMetadataZone() {
    this.toggleMetadataZone(true);
  }
  
  public void deactivateMetadataZone() {
    this.toggleMetadataZone(false);
  }
  
  public void updateMetadataFileLocation(final String location) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        metadataFileLocation.setText(location);        
      }
    });
  }
  
  private class PickPicturesFilesAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    
    private final AddController controller;

    public PickPicturesFilesAction(final AddController controller) {
      super("pickpictures");
      this.controller = controller;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
      final int returnVal = picturesFilesChooser.showOpenDialog(JOptionPane.getRootFrame());
      if (returnVal == JFileChooser.APPROVE_OPTION && picturesFilesChooser.getSelectedFiles().length > 0) {
        controller.setPicturesFiles(picturesFilesChooser.getSelectedFiles());
      }
    }
  }
  
  private class UseExternalMetadataCheckboxHandler extends AbstractAction implements ItemListener {
    private static final long serialVersionUID = 1L;
    
    private final AddController controller;

    public UseExternalMetadataCheckboxHandler(final AddController controller) {
      super(UI.BUNDLE.getString("addpictures.metadata.use"));
      this.controller = controller;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
    };
    
    @Override
    public void itemStateChanged(final ItemEvent e) {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          switch (e.getStateChange()) {
          case ItemEvent.SELECTED:
            controller.setUseMetadata(true);
            break;
          case ItemEvent.DESELECTED:
            controller.setUseMetadata(false);
            break;
          }
        }
      });
    }
  }
  
  private class PickMetadataFileAction extends BaseAction {
    private static final long serialVersionUID = 1L;

    public PickMetadataFileAction() {
      super("pickmetadata");
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
      final int returnVal = metadataFileChooser.showOpenDialog(JOptionPane.getRootFrame());
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        controller.setMetadataFile(Paths.get(metadataFileChooser.getSelectedFile().toURI()));
      }
    }
  }
  
  private class CSVAccessoryPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    
    public CSVAccessoryPanel() {
      final GroupLayout layout = new GroupLayout(this);
      layout.setAutoCreateContainerGaps(true);
      layout.setAutoCreateGaps(true);
      this.setLayout(layout);
      
      this.setBorder(BorderFactory.createTitledBorder(UI.BUNDLE.getString("addpictures.metadata.csv.title")));
      
      final MaskFormatter singleCharFormatter = new MaskFormatter();
      try {
        singleCharFormatter.setMask("*");
        singleCharFormatter.setAllowsInvalid(false);
        singleCharFormatter.setCommitsOnValidEdit(false);
      } catch (final ParseException e) {
        throw new Error("Bad formatter", e);
      }
      
      final JLabel separatorLabel = new JLabel(UI.BUNDLE.getString("addpictures.metadata.csv.separator"));
      final JFormattedTextField separatorField = new JFormattedTextField(singleCharFormatter);
      separatorLabel.setLabelFor(separatorField);
      separatorField.setToolTipText(UI.BUNDLE.getString("addpictures.metadata.csv.separator.tooltip"));
      separatorField.setText(String.valueOf(controller.getSeparator()));
      separatorField.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void removeUpdate(final DocumentEvent e) {
          separatorChanged(e);
        }
        
        @Override
        public void insertUpdate(final DocumentEvent e) {
          separatorChanged(e);
        }
        
        @Override
        public void changedUpdate(final DocumentEvent e) {
          separatorChanged(e);
        }
      });
      
      final JLabel quoteLabel = new JLabel(UI.BUNDLE.getString("addpictures.metadata.csv.quote"));
      final JFormattedTextField quoteField = new JFormattedTextField(singleCharFormatter);
      quoteLabel.setLabelFor(quoteField);
      quoteField.setToolTipText(UI.BUNDLE.getString("addpictures.metadata.csv.quote.tooltip"));
      quoteField.setText(String.valueOf(controller.getQuote()));
      quoteField.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void removeUpdate(final DocumentEvent e) {
          quoteChanged(e);
        }
        
        @Override
        public void insertUpdate(final DocumentEvent e) {
          quoteChanged(e);
        }
        
        @Override
        public void changedUpdate(final DocumentEvent e) {
          quoteChanged(e);
        }
      });
      
      final JLabel escapeLabel = new JLabel(UI.BUNDLE.getString("addpictures.metadata.csv.escape"));
      final JFormattedTextField escapeField = new JFormattedTextField(singleCharFormatter);
      escapeLabel.setLabelFor(escapeField);
      escapeField.setToolTipText(UI.BUNDLE.getString("addpictures.metadata.csv.escape.tooltip"));
      escapeField.setText(String.valueOf(controller.getEscape()));
      escapeField.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void removeUpdate(final DocumentEvent e) {
          escapeChanged(e);
        }
        
        @Override
        public void insertUpdate(final DocumentEvent e) {
          escapeChanged(e);
        }
        
        @Override
        public void changedUpdate(final DocumentEvent e) {
          escapeChanged(e);
        }
      });
      
      final JLabel skipLinesLabel = new JLabel(UI.BUNDLE.getString("addpictures.metadata.csv.skipLines"));
      final JSpinner skipLinesField = new JSpinner(new SpinnerNumberModel(controller.getSkipLines(), 0, Integer.MAX_VALUE, 1) {
        private static final long serialVersionUID = 1L;
        @Override
        public void setValue(final Object value) {
          controller.setSkipLines((int) value);
          super.setValue(value);
        }
      });
      skipLinesLabel.setLabelFor(skipLinesField);
      skipLinesField.setToolTipText(UI.BUNDLE.getString("addpictures.metadata.csv.skipLines.tooltip"));
      
      final JCheckBox strictQuotesBox = new JCheckBox(UI.BUNDLE.getString("addpictures.metadata.csv.strictQuotes"), controller.isStrictQuotes());
      strictQuotesBox.addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(final ChangeEvent e) {
          controller.setStrictQuotes(strictQuotesBox.isSelected());
        }
      });
      strictQuotesBox.setToolTipText(UI.BUNDLE.getString("addpictures.metadata.csv.strictQuotes.tooltip"));
      
      final JCheckBox ignoreLeadingWhiteSpaceBox = new JCheckBox(UI.BUNDLE.getString("addpictures.metadata.csv.ignoreLeadingWhiteSpace"), controller.isIgnoreLeadingWhiteSpace());
      ignoreLeadingWhiteSpaceBox.addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(final ChangeEvent e) {
          controller.setIgnoreLeadingWhiteSpace(ignoreLeadingWhiteSpaceBox.isSelected());
        }
      });
      
      final JLabel charsetLabel = new JLabel(UI.BUNDLE.getString("addpictures.metadata.csv.charset"));
      final JComboBox<Charset> charsetField = new JComboBox<>(new Charset[] {
          Charsets.ISO_8859_1,
          Charsets.UTF_8,
          Charsets.US_ASCII
      });
      charsetLabel.setLabelFor(charsetField);
      charsetField.setEditable(true);
      charsetField.setSelectedItem(controller.getCharset());
      charsetField.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          final Object item = charsetField.getSelectedItem();
          if (item instanceof Charset) {
            controller.setCharset((Charset) item);
          }
        }
      });
      
      layout.setVerticalGroup(
        layout.createSequentialGroup()
        .addComponent(separatorLabel)
        .addComponent(separatorField)
        .addComponent(quoteLabel)
        .addComponent(quoteField)
        .addComponent(escapeLabel)
        .addComponent(escapeField)
        .addComponent(skipLinesLabel)
        .addComponent(skipLinesField)
        .addComponent(strictQuotesBox)
        .addComponent(ignoreLeadingWhiteSpaceBox)
        .addComponent(charsetLabel)
        .addComponent(charsetField)
      );
      layout.setHorizontalGroup(
        layout.createParallelGroup()
        .addComponent(separatorLabel)
        .addComponent(separatorField)
        .addComponent(quoteLabel)
        .addComponent(quoteField)
        .addComponent(escapeLabel)
        .addComponent(escapeField)
        .addComponent(skipLinesLabel)
        .addComponent(skipLinesField)
        .addComponent(strictQuotesBox)
        .addComponent(ignoreLeadingWhiteSpaceBox)
        .addComponent(charsetLabel)
        .addComponent(charsetField)
      );
    }
    
    private void separatorChanged(final DocumentEvent e) {
      controller.setSeparator(getFirstChar(e));
    }
    
    private void quoteChanged(final DocumentEvent e) {
      controller.setQuote(getFirstChar(e));
    }
    
    private void escapeChanged(final DocumentEvent e) {
      controller.setEscape(getFirstChar(e));
    }
    
    private char getFirstChar(final DocumentEvent e) {
      final Document document = e.getDocument();
      try {
        return document.getLength() == 0 ? 0 : document.getText(0, document.getLength()).charAt(0);
      } catch (final BadLocationException e1) {
        return 0;
      }
    }
  }
}