package comeon.ui.add;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
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
    
    private final JButton pickFileButton;
    
    private final JTextField pictureExpression;
    
    private final JTextField metatadataExpression;
    
    private File file;

    public FilesPanel() {
      super(new GridBagLayout());
      
      this.filesChooser = new JFileChooser();
      filesChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      filesChooser.setMultiSelectionEnabled(true);
      filesChooser.setFileFilter(new FileNameExtensionFilter(UI.BUNDLE.getString("action.addpictures.filter"), "jpg", "jpeg"));
      this.fileChooser = new JFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fileChooser.setMultiSelectionEnabled(false);
      fileChooser.setFileFilter(new FileNameExtensionFilter(UI.BUNDLE.getString("addpictures.metadata.filter"), "csv"));

      final JLabel filesListLabel = new JLabel("Images");
      this.filesListModel = new DefaultListModel<>();
      final JList<File> filesList = new JList<>(filesListModel);
      filesList.setPrototypeCellValue(new File(Strings.repeat("x", 30)));
      
      final UseExternalMetadataCheckboxHandler checkboxHandler = new UseExternalMetadataCheckboxHandler();
      this.metatadataCheckbox = new JCheckBox(checkboxHandler);
      metatadataCheckbox.addItemListener(checkboxHandler);

      final JLabel metadataFileLabel = new JLabel("Métadonnées");
      final JTextField metadataFileLocation = new JTextField(30);
      this.pickFileButton = new JButton(new PickMetadataFileAction());
      this.pictureExpression = new JTextField(20);
      this.metatadataExpression = new JTextField(20);

      final GridBagConstraints constraints = new GridBagConstraints();
      
      this.add(filesListLabel, constraints);
      constraints.gridx = 1;
      constraints.gridwidth= 3;
      constraints.fill = GridBagConstraints.BOTH;
      this.add(new JScrollPane(filesList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), constraints);
      constraints.gridx = 4;
      constraints.gridwidth = 1;
      constraints.gridheight = 1;
      constraints.fill = GridBagConstraints.NONE;
      this.add(new JButton(new PickPicturesFilesAction()), constraints);
      
      constraints.gridy = 1;

      constraints.gridx = 1;
      constraints.gridwidth = 3;
      this.add(metatadataCheckbox, constraints);
      
      constraints.gridy = 2;
      
      constraints.gridx = 0;
      constraints.gridwidth = 1;
      this.add(metadataFileLabel, constraints);
      constraints.gridx = 1;
      constraints.gridwidth = 3;
      constraints.fill = GridBagConstraints.BOTH;
      this.add(metadataFileLocation, constraints);
      constraints.gridx = 4;
      constraints.gridwidth = 1;
      constraints.fill = GridBagConstraints.NONE;
      this.add(new JButton(new PickPicturesFilesAction()), constraints);
      
      constraints.gridy = 3;
      
      constraints.gridx = 0;
      this.add(new JLabel(UI.BUNDLE.getString("addpictures.metadata.match.label")), constraints);
      constraints.gridx = 1;
      constraints.fill = GridBagConstraints.BOTH;
      this.add(pictureExpression, constraints);
      constraints.gridx = 2;
      constraints.fill = GridBagConstraints.NONE;
      this.add(new JLabel(UI.BUNDLE.getString("addpictures.metadata.match.symbol")), constraints);
      constraints.gridx = 3;
      constraints.fill = GridBagConstraints.BOTH;
      this.add(metatadataExpression, constraints);
      
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
      return metatadataExpression.getText();
    }
    
    public void activateMetadataZone() {
      pickFileButton.setEnabled(true);
      pictureExpression.setEnabled(true);
      metatadataExpression.setEnabled(true);
    }
    
    public void deactivateMetadataZone() {
      pickFileButton.setEnabled(false);
      pictureExpression.setEnabled(false);
      metatadataExpression.setEnabled(false);
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
