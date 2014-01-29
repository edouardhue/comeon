package comeon.ui.add;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.google.common.base.Strings;
import comeon.ui.UI;

class FilesPanel extends JPanel {
  private static final int MEDIUM_PROTOTYPE_LENGTH = 20;

  private static final String PROTOTYPE_CHAR = "x";

  private static final int LONG_PROTOTYPE_LENGTH = 30;

  private static final long serialVersionUID = 1L;

  private final JFileChooser filesChooser;

  private final JCheckBox metatadataCheckbox;
  
  private final JFileChooser fileChooser;
  
  private final JLabel metadataFileLabel;
  
  private final JTextField metadataFileLocation;
  
  private final JButton pickMetadataFileButton;
  
  private final JLabel metadataMatchLabel;
  
  private final JTextField pictureExpression;
  
  private final JLabel metadataMatchSymbol;
  
  private final JComboBox<String> metadataExpression;
  
  private final Controller controller;
  
  public FilesPanel(final Controller controller) {
    super();
    this.controller = controller;
    
    final GroupLayout layout = new GroupLayout(this);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    this.setLayout(layout);
    
    this.filesChooser = new JFileChooser();
    filesChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    filesChooser.setMultiSelectionEnabled(true);
    filesChooser.setFileFilter(new FileNameExtensionFilter(UI.BUNDLE.getString("action.addpictures.filter"), "jpg", "jpeg"));
    this.fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
    fileChooser.setFileFilter(new FileNameExtensionFilter(UI.BUNDLE.getString("addpictures.metadata.filter"), "csv"));

    final JLabel filesListLabel = new JLabel(UI.BUNDLE.getString("addpictures.pictures.label"));

    final JList<File> filesList = new JList<>(controller.getPicturesListModel());
    filesList.setPrototypeCellValue(new File(Strings.repeat(PROTOTYPE_CHAR, LONG_PROTOTYPE_LENGTH)));
    final JScrollPane filesListPanel = new JScrollPane(filesList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    final JButton pickPicturesFilesButton = new JButton(new PickPicturesFilesAction(controller));

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
                    .addComponent(metadataFileLabel)
                    .addComponent(metadataMatchLabel)
                )
                .addGroup(
                    layout.createParallelGroup()
                    .addComponent(metadataFileLocation)
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
  
  private class PickPicturesFilesAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    
    private final Controller controller;

    public PickPicturesFilesAction(final Controller controller) {
      super(UI.BUNDLE.getString("addpictures.pictures.pick"));
      this.controller = controller;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
      final int returnVal = filesChooser.showOpenDialog(JOptionPane.getRootFrame());
      if (returnVal == JFileChooser.APPROVE_OPTION && filesChooser.getSelectedFiles().length > 0) {
        controller.setPicturesFiles(filesChooser.getSelectedFiles());
      }
    }
  }
  
  private class UseExternalMetadataCheckboxHandler extends AbstractAction implements ItemListener {
    private static final long serialVersionUID = 1L;
    
    private final Controller controller;

    public UseExternalMetadataCheckboxHandler(final Controller controller) {
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
  
  private class PickMetadataFileAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public PickMetadataFileAction() {
      super(UI.BUNDLE.getString("addpictures.metadata.pick"));
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
      final int returnVal = fileChooser.showOpenDialog(JOptionPane.getRootFrame());
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        controller.setMetadataFile(fileChooser.getSelectedFile());
      }
    }
  }
}