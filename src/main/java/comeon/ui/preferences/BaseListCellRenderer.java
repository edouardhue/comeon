package comeon.ui.preferences;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

abstract class BaseListCellRenderer<T> extends DefaultListCellRenderer {

  private static final long serialVersionUID = 1L;

  @Override
  public final Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
      boolean isSelected, boolean cellHasFocus) {
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
