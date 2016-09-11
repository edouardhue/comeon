package comeon.ui.preferences;

import javax.swing.*;
import java.awt.*;

public abstract class BaseListCellRenderer<T> extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public final Component getListCellRendererComponent(final JList<? extends Object> list, final Object value,
                                                        final int index, final boolean isSelected,
                                                        final boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        this.setFont(this.getFont().deriveFont(Font.PLAIN));
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        @SuppressWarnings("unchecked")
        final T v = (T) value;
        this.customizeComponent(v);
        return this;
    }

    protected abstract void customizeComponent(final T value);
}
