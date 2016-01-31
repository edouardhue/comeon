package comeon.ui.media.metadata;

import javax.swing.table.TableModel;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

public final class MediaMetadataTable extends AbstractMetadataTable<DynaBean> {
  private static final long serialVersionUID = 1L;

  public MediaMetadataTable(final String directoryName, final DynaBean directoryContent) {
    super(directoryName, directoryContent);
  }

  @Override
  protected TableModel getTableModel(final DynaBean content) {
    return new MediaMetadataTableModel(content);
  }
  
  private static final class MediaMetadataTableModel extends AbstractMetadataTableModel {
    private static final long serialVersionUID = 1L;

    private final DynaProperty[] properties;

    private final DynaBean content;

    private MediaMetadataTableModel(final DynaBean directoryContent) {
      this.properties = directoryContent.getDynaClass().getDynaProperties();
      this.content = directoryContent;
    }

    @Override
    public int getRowCount() {
      return properties.length;
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
