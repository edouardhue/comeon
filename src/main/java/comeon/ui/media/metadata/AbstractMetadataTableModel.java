package comeon.ui.media.metadata;

import javax.swing.table.AbstractTableModel;

abstract class AbstractMetadataTableModel extends AbstractTableModel {

  private static final long serialVersionUID = 1L;

  @Override
  public final int getColumnCount() {
    return 2;
  }

}
