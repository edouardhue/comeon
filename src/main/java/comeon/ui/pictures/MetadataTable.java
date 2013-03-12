package comeon.ui.pictures;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

final class MetadataTable extends JPanel {
  private static final long serialVersionUID = 1L;
  
  public MetadataTable(final String directoryName, final DynaBean directoryContent) {
    super(new BorderLayout());
    final JLabel title = new JLabel(directoryName);
    this.add(title, BorderLayout.NORTH);
    final JTable table = new JTable(new TableModel(directoryContent));
    table.setCellSelectionEnabled(false);
    table.setColumnSelectionAllowed(false);
    table.getColumnModel().getColumn(0).setCellRenderer(new TooltipCellRenderer());
    table.getColumnModel().getColumn(1).setCellRenderer(new TooltipCellRenderer());
    this.add(table, BorderLayout.CENTER);
  }

  private static final class TooltipCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      final JLabel comp = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      comp.setToolTipText(String.valueOf(value));
      return comp;
    }
  }
  
  private static final class TableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private final DynaProperty[] properties;
    
    private final DynaBean content;
    
    private TableModel(final DynaBean directoryContent) {
      this.properties = directoryContent.getDynaClass().getDynaProperties();
      this.content = directoryContent;
    }

    @Override
    public int getRowCount() {
      return properties.length;
    }

    @Override
    public int getColumnCount() {
      return 2;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
      final Object value;
      final DynaProperty property = this.properties[rowIndex];
      switch (columnIndex) {
      case 0:
        value = property.getName();
        break;
      case 1:
        value = content.get(property.getName());
        break;
      default:
         throw new IndexOutOfBoundsException("No such column: " + columnIndex); 
      }
      return value;
    }
    
  }
}
