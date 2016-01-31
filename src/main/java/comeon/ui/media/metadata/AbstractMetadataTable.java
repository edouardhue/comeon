package comeon.ui.media.metadata;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

abstract class AbstractMetadataTable<T> extends JPanel {

  private static final long serialVersionUID = 1L;

  public AbstractMetadataTable(final String title, final T content) {
    super(new BorderLayout());
    final JLabel tableTitle = new JLabel(title);
    this.add(tableTitle, BorderLayout.NORTH);
    final JTable table = new JTable(this.getTableModel(content));
    table.setCellSelectionEnabled(false);
    table.setColumnSelectionAllowed(false);
    table.getColumnModel().getColumn(0).setCellRenderer(new TooltipCellRenderer());
    table.getColumnModel().getColumn(1).setCellRenderer(new TooltipCellRenderer());
    this.add(table, BorderLayout.CENTER);
  }
  
  protected abstract TableModel getTableModel(final T content);

  private static final class TooltipCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
        final boolean hasFocus, final int row, final int column) {
      final JLabel comp = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      comp.setToolTipText(String.valueOf(value));
      return comp;
    }
  }
}
