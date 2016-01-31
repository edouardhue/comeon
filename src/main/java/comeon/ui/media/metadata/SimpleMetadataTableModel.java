package comeon.ui.media.metadata;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Collections;
import java.util.List;

abstract class SimpleMetadataTableModel<T> extends AbstractMetadataTableModel {

  private static final long serialVersionUID = 1L;

  private final List<Entry> values;

  public SimpleMetadataTableModel(final T content) {
    List<Entry> values = this.getValues(content);
    Collections.sort(values);
    this.values = values;
  }
  
  protected abstract List<Entry> getValues(final T content);

  @Override
  public int getRowCount() {
    return values.size();
  }

  @Override
  public Object getValueAt(final int rowIndex, final int columnIndex) {
    final Entry entry = values.get(rowIndex);
    final String value;
    switch (columnIndex) {
    case 0:
      value = entry.getKey();
      break;
    case 1:
      value = entry.getValue();
      break;
    default:
      throw new IllegalArgumentException();
    }
    return value;
  }

  static final class Entry implements Comparable<Entry> {
    private static final Collator collator = Collator.getInstance();

    private final String key;

    private final String value;

    private final CollationKey collationKey;

    public Entry(final String key, final String value) {
      this.key = key;
      this.value = value;
      this.collationKey = collator.getCollationKey(key);
    }

    public String getKey() {
      return key;
    }

    public String getValue() {
      return value;
    }

    @Override
    public int compareTo(final Entry o) {
      return collationKey.compareTo(o.collationKey);
    }
  }

}
