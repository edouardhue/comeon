package comeon.ui.pictures.metadata;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.table.TableModel;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExternalMetadataTable extends AbstractMetadataTable<Object> {

  private static final long serialVersionUID = 1L;

  public ExternalMetadataTable(final String title, final Object content) {
    super(title, content);
  }

  @Override
  protected TableModel getTableModel(final Object content) {
    return new ExternalMetadataTableModel(content);
  }

  private static final class ExternalMetadataTableModel extends AbstractPictureMetadataTableModel {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalMetadataTableModel.class);

    private static final long serialVersionUID = 1L;

    private final List<String[]> values;

    @SuppressWarnings("unchecked")
    public ExternalMetadataTableModel(final Object content) {
      List<String[]> values;
      try {
        final Map<String, Class<?>> properties = BeanUtils.describe(content);
        properties.remove("class");
        values = new ArrayList<>(properties.size());
        for (final Map.Entry<String, Class<?>> property : properties.entrySet()) {
          final String propertyName = property.getKey();
          final String propertyValue = BeanUtils.getProperty(content, propertyName);
          values.add(new String[] {propertyName, propertyValue});
        }
      } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        LOGGER.warn("Could not extract properties", e);
        values = Collections.emptyList();
      }
      this.values = values;
    }

    @Override
    public int getRowCount() {
      return values.size();
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
      return values.get(rowIndex)[columnIndex];
    }

  }
}
