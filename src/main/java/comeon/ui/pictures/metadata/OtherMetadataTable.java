package comeon.ui.pictures.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.table.TableModel;

public final class OtherMetadataTable extends AbstractMetadataTable<Map<String, Object>> {

  private static final long serialVersionUID = 1L;

  public OtherMetadataTable(final String title, final Map<String, Object> content) {
    super(title, content);
  }

  @Override
  protected TableModel getTableModel(final Map<String, Object> content) {
    return new OtherMetadataTableModel(content);
  }

  private static final class OtherMetadataTableModel extends SimpleMetadataTableModel<Map<String, Object>> {

    private static final long serialVersionUID = 1L;

    public OtherMetadataTableModel(final Map<String, Object> content) {
      super(content);
    }

    @Override
    protected List<Entry> getValues(final Map<String, Object> content) {
      final List<Entry> values = new ArrayList<>(content.size());
      for (final Map.Entry<String, Object> entry : content.entrySet()) {
        final String value;
        if (entry.getValue().getClass().isArray()) {
          value = Arrays.toString((Object[]) entry.getValue());
        } else {
          value = String.valueOf(entry.getValue());
        }
        values.add(new Entry(entry.getKey(), value));
      }
      return values;
    }
    
  }
}
