package comeon.ui.preferences;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.google.common.io.Files;
import comeon.model.Template;
import comeon.model.TemplateKind;
import comeon.ui.UI;

public final class TemplatesPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  
  private final TableModel tableModel;
  
  private final JTable table;
  
  public TemplatesPanel(final List<Template> templates) {
    super(new BorderLayout());
    this.tableModel = new TableModel(templates);
    this.table = new JTable(tableModel);
    this.add(new JScrollPane(table), BorderLayout.CENTER);
    final JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER));
    // TODO add icons
    toolbar.add(new JButton(new AddTemplateAction()));
    toolbar.add(new JButton(new RemoveTemplateAction()));
    this.add(toolbar, BorderLayout.SOUTH);
  }
  
  public List<Template> getTemplates() {
    return tableModel.getTemplates();
  }
  
  // TODO i18n
  private final class AddTemplateAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    
    private final JFileChooser chooser;

    public AddTemplateAction() {
      super(UI.BUNDLE.getString("prefs.templates.add"));
      this.chooser = new JFileChooser();
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
      final int chooserReturnVal = chooser.showOpenDialog(TemplatesPanel.this);
      if (JFileChooser.APPROVE_OPTION == chooserReturnVal) {
        final File file = chooser.getSelectedFile();
        try {
          final Charset charset = Charset.forName(JOptionPane.showInputDialog(TemplatesPanel.this, "Charset ?"));
          final String templateText = Files.toString(file, charset);
          final String name = JOptionPane.showInputDialog(TemplatesPanel.this, "Name ?");
          final String description = JOptionPane.showInputDialog(TemplatesPanel.this, "Description ?");
          final TemplateKind kind = (TemplateKind) JOptionPane.showInputDialog(TemplatesPanel.this, "Kind ?", "Kind", JOptionPane.QUESTION_MESSAGE, null,
              TemplateKind.values(), TemplateKind.values()[0]);
          final Template template = new Template(name, description, file, charset, templateText, kind);
          tableModel.addTemplate(template);
        } catch (final IOException ex) {
          JOptionPane.showMessageDialog(TemplatesPanel.this, ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    }
  }
  
  private final class RemoveTemplateAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public RemoveTemplateAction() {
      super(UI.BUNDLE.getString("prefs.templates.remove"));
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
      final int selectedRow = table.getSelectedRow();
      if (selectedRow != -1) {
        tableModel.removeRow(selectedRow);
      }
    }
  }
  
  private static final class TableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private final List<Template> templates;
    
    public TableModel(final List<Template> templates) {
      this.templates = new ArrayList<>(templates);
    }

    @Override
    public int getRowCount() {
      return templates.size();
    }

    @Override
    public int getColumnCount() {
      return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      final Template template = templates.get(rowIndex);
      final String value;
      switch (columnIndex) {
      case 0:
        value = template.getName();
        break;
      case 1:
        value = template.getDescription();
        break;
      case 2:
        value = template.getFile().getAbsolutePath();
        break;
      case 3:
        value = template.getKind().name();
        break;
      default:
        throw new IllegalArgumentException();
      }
      return value;
    }
    
    private List<Template> getTemplates() {
      return new ArrayList<>(templates);
    }
    
    private void addTemplate(final Template template) {
      templates.add(template);
      final int lastRow = templates.size() - 1;
      this.fireTableRowsInserted(lastRow, lastRow);
    }
    
    private void removeRow(final int row) {
        templates.remove(row);
        this.fireTableRowsDeleted(row, row);
    }
  }

}
