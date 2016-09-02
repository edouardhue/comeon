package comeon.ui;

import javax.swing.table.TableColumn;

public final class ComeOnTableColumn extends TableColumn {

    private static final long serialVersionUID = 1L;

    public ComeOnTableColumn(final int modelIndex, final Object headerValue) {
        super(modelIndex);
        this.setHeaderValue(headerValue);
    }
}
