package comeon.ui.pictures;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

final class MetadataTable extends JPanel {
  private static final long serialVersionUID = 1L;
  
  public MetadataTable(final String directoryName, final DynaBean directoryContent) {
    super(new BorderLayout());
    final JLabel title = new JLabel(directoryName);
    this.add(title, BorderLayout.NORTH);
    final JTable table = new JTable(new Model(directoryContent));
    table.setCellSelectionEnabled(false);
    table.setColumnSelectionAllowed(false);
    this.add(table, BorderLayout.CENTER);
  }

  private static final class Model extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private final DynaProperty[] properties;
    
    private final DynaBean content;
    
    private Model(final DynaBean directoryContent) {
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
