package comeon.ui.media.metadata;

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

  private static final class ExternalMetadataTableModel extends SimpleMetadataTableModel<Object> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalMetadataTableModel.class);

    private static final String CLASS_PROPERTY_NAME = "class";

    public ExternalMetadataTableModel(final Object content) {
      super(content);
    }
    
    @Override
    protected List<Entry> getValues(Object content) {
      List<Entry> values;
      try {
        final Map<String, String> properties = BeanUtils.describe(content);
        properties.remove(CLASS_PROPERTY_NAME);
        values = new ArrayList<>(properties.size());
        for (final Map.Entry<String, String> property : properties.entrySet()) {
          final String propertyName = property.getKey();
          final String propertyValue = BeanUtils.getProperty(content, propertyName);
          values.add(new Entry(propertyName, propertyValue));
        }
      } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        LOGGER.warn("Could not extract properties", e);
        values = Collections.emptyList();
      }
      return values;
    }

  }
}
