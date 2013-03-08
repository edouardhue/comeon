package comeon.ui.pictures;

import java.awt.BorderLayout;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.metadata.Directory;
import com.drew.metadata.TagDescriptor;
import comeon.MetadataHelper;

final class MetadataTable extends JPanel {
  private static final Logger LOGGER = LoggerFactory.getLogger(MetadataTable.class);

  private static final long serialVersionUID = 1L;
  
  public MetadataTable(final Directory dir) {
    super(new BorderLayout());
    final JLabel title = new JLabel(dir.getName());
    this.add(title, BorderLayout.NORTH);
    final JTable table = new JTable(new Model(dir));
    this.add(table, BorderLayout.CENTER);
  }

  private static final class Model extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
    private final List<PropertyDescriptor> propDescriptors;
    
    private final TagDescriptor<?> tagDescriptor;
    
    private Model(final Directory dir) {
      this.tagDescriptor = MetadataHelper.getDescriptor(dir);
      final PropertyDescriptor[] allPropDescriptors = PropertyUtils.getPropertyDescriptors(tagDescriptor);
      this.propDescriptors = new ArrayList<>(allPropDescriptors.length);
      for (final PropertyDescriptor propDescriptor : allPropDescriptors) {
        if (propDescriptor.getReadMethod() != null && propDescriptor.getReadMethod().getDeclaringClass().equals(tagDescriptor.getClass())) {
          propDescriptors.add(propDescriptor);
        }
      }
    }

    @Override
    public int getRowCount() {
      return propDescriptors.size();
    }

    @Override
    public int getColumnCount() {
      return 2;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
      Object value;
      final PropertyDescriptor descriptor = propDescriptors.get(rowIndex);
      switch (columnIndex) {
      case 0:
        value = descriptor.getName();
        break;
      case 1:
        try {
          value = descriptor.getReadMethod().invoke(tagDescriptor, new Object[0]);
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          LOGGER.debug("Can't read property {}", descriptor.getName(), e);
          value = null;
        }
        break;
      default:
        throw new IndexOutOfBoundsException("Only two columns here");
      }
      return value;
    }
    
  }
}
