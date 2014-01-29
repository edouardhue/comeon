package comeon.ui.pictures.metadata;

import javax.swing.table.AbstractTableModel;

abstract class AbstractPictureMetadataTableModel extends AbstractTableModel {

  private static final long serialVersionUID = 1L;

  @Override
  public final int getColumnCount() {
    return 2;
  }

}
