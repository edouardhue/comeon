package comeon.ui.add;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.common.base.Strings;
import comeon.ui.UI;

public final class AddPicturesDialog extends JOptionPane {

  private static final long serialVersionUID = 1L;

  private final JDialog dialog;
  
  public AddPicturesDialog() {
    super(null, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
    this.setMessage(new FilesPanel());
    this.dialog = this.createDialog(JOptionPane.getRootFrame(), UI.BUNDLE.getString("action.addpictures.title"));
  }

  public int showDialog() {
    this.dialog.setVisible(true);
    return ((Integer) this.getValue()).intValue();
  }
  
  private class FilesPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final JFileChooser filesChooser;
    
    private final DefaultListModel<File> filesListModel;

    private final JCheckBox metatadataCheckbox;
    
    private final JFileChooser fileChooser;
    
    private final JLabel metadataFileLabel;
    
    private final JTextField metadataFileLocation;
    
    private final JButton pickMetadataFileButton;
    
    private final JLabel metadataMatchLabel;
    
    private final JTextField pictureExpression;
    
    private final JLabel metadataMatchSymbol;
    
    private final JTextField metadataExpression;
    
    private File file;

    public FilesPanel() {
      super();
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

      this.filesListModel = new DefaultListModel<>();
      final JList<File> filesList = new JList<>(filesListModel);
      filesList.setPrototypeCellValue(new File(Strings.repeat("x", 30)));
      final JScrollPane filesListPanel = new JScrollPane(filesList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      
      final JButton pickPicturesFilesButton = new JButton(new PickPicturesFilesAction());

      final UseExternalMetadataCheckboxHandler checkboxHandler = new UseExternalMetadataCheckboxHandler();
      this.metatadataCheckbox = new JCheckBox(checkboxHandler);
      metatadataCheckbox.addItemListener(checkboxHandler);

      this.metadataFileLabel = new JLabel(UI.BUNDLE.getString("addpictures.metadata.label"));
      this.metadataFileLocation = new JTextField(30);
      this.pickMetadataFileButton = new JButton(new PickMetadataFileAction());
      this.metadataMatchLabel = new JLabel(UI.BUNDLE.getString("addpictures.metadata.match.label"));
      this.pictureExpression = new JTextField(20);
      this.metadataExpression = new JTextField(20);
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
    
    public boolean getUseMetadata() {
      return metatadataCheckbox.isSelected();
    }
    
    public File getFile() {
      return this.file;
    }
    
    public String getPictureExpression() {
      return pictureExpression.getText();
    }
    
    public String getMetadataExpression() {
      return metadataExpression.getText();
    }
    
    private void toggleMetadataZone(final boolean state) {
      pickMetadataFileButton.setEnabled(state);
      metadataFileLabel.setEnabled(state);
      metadataFileLocation.setEnabled(state);
      metadataMatchLabel.setEnabled(state);
      pictureExpression.setEnabled(state);
      metadataMatchSymbol.setEnabled(state);
      metadataExpression.setEnabled(state);
    }
    
    public void activateMetadataZone() {
      this.toggleMetadataZone(true);
    }
    
    public void deactivateMetadataZone() {
      this.toggleMetadataZone(false);
    }
    
    private class PickPicturesFilesAction extends AbstractAction {
      private static final long serialVersionUID = 1L;

      public PickPicturesFilesAction() {
        super(UI.BUNDLE.getString("addpictures.pictures.pick"));
      }
      
      @Override
      public void actionPerformed(final ActionEvent e) {
        final int returnVal = filesChooser.showOpenDialog(JOptionPane.getRootFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION && filesChooser.getSelectedFiles().length > 0) {
          filesListModel.removeAllElements();
          final File[] files = filesChooser.getSelectedFiles();
          for (final File file : files) {
            filesListModel.addElement(file);
          }
        }
      }
    }
    
    private class UseExternalMetadataCheckboxHandler extends AbstractAction implements ItemListener {
      private static final long serialVersionUID = 1L;

      public UseExternalMetadataCheckboxHandler() {
        super(UI.BUNDLE.getString("addpictures.metadata.use"));
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
              activateMetadataZone();
              break;
            case ItemEvent.DESELECTED:
              deactivateMetadataZone();
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
          file = fileChooser.getSelectedFile();
        }
      }
    }
  }
}
