package comeon.ui.preferences;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;

import comeon.model.Template;
import comeon.templates.Templates;
import comeon.ui.ComeOnTableColumn;
import comeon.ui.UI;

public final class TemplatesPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private final TemplatesTableModel tableModel;

  private final JTable table;
  
  private final Templates templates;

  public TemplatesPanel(final Templates templates) {
    super(new BorderLayout());
    this.templates = templates;
    this.tableModel = new TemplatesTableModel(templates.getTemplates());
    this.table = new JTable(tableModel, new TemplatesColumnModel());
    this.add(new JScrollPane(table), BorderLayout.CENTER);
    final JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER));
    // TODO add icons
    toolbar.add(new JButton(new AddTemplateAction()));
    toolbar.add(new JButton(new EditTemplateAction()));
    toolbar.add(new JButton(new RemoveTemplateAction()));
    this.add(toolbar, BorderLayout.SOUTH);
  }

  public List<Template> getTemplates() {
    return tableModel.getTemplates();
  }

  private final class AddTemplateAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public AddTemplateAction() {
      super(UI.BUNDLE.getString("prefs.templates.add"));
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
      final TemplatePanel panel = new TemplatePanel(null, templates.getTemplateKinds());
      final int value = panel.showDialog();
      if (value == JOptionPane.OK_OPTION) {
        final Template template = panel.getTemplate();
        tableModel.addTemplate(template);
      }
    }
  }

  private final class EditTemplateAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    
    public EditTemplateAction() {
      super(UI.BUNDLE.getString("prefs.templates.edit"));
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
      final int selectedRow = table.getSelectedRow();
      if (selectedRow != -1) {
        final Template selectedTemplate = tableModel.getTemplates().get(selectedRow);
        final TemplatePanel panel = new TemplatePanel(selectedTemplate, templates.getTemplateKinds());
        final int value = panel.showDialog();
        if (value == JOptionPane.OK_OPTION) {
          final Template editedTemplate = panel.getTemplate();
          tableModel.replaceTemplate(selectedRow, editedTemplate);
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
  
  private static final class TemplatesColumnModel extends DefaultTableColumnModel {

    private static final long serialVersionUID = 1L;

    public TemplatesColumnModel() {
      super.tableColumns.addAll(Arrays.asList(
          new ComeOnTableColumn(0, UI.BUNDLE.getString("prefs.templates.name")),
          new ComeOnTableColumn(1, UI.BUNDLE.getString("prefs.templates.description")),
          new ComeOnTableColumn(2, UI.BUNDLE.getString("prefs.templates.kind")),
          new ComeOnTableColumn(3, UI.BUNDLE.getString("prefs.templates.charset"))
      ));
    }
  }

  private static final class TemplatesTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private final List<Template> templates;

    public TemplatesTableModel(final List<Template> templates) {
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
    public Object getValueAt(final int rowIndex, final int columnIndex) {
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
        value = template.getKind().getClass().getSimpleName();
        break;
      case 3:
        value = template.getCharset().displayName();
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
    
    private void replaceTemplate(final int row, final Template template) {
      templates.set(row, template);
      this.fireTableRowsUpdated(row, row);
    }

    private void removeRow(final int row) {
      templates.remove(row);
      this.fireTableRowsDeleted(row, row);
    }
  }

}
