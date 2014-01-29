package comeon.core;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.cglib.beans.BeanGenerator;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.MappingStrategy;

public final class CGLibMappingStrategy implements MappingStrategy<Object> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CGLibMappingStrategy.class);
  
  private Class<?> clazz;

  private PropertyDescriptor[] properties;

  @Override
  public PropertyDescriptor findDescriptor(final int col) throws IntrospectionException {
    return properties[col];
  }

  @Override
  public Object createBean() throws InstantiationException, IllegalAccessException {
    return clazz.newInstance();
  }

  @Override
  public void captureHeader(final CSVReader reader) throws IOException {
    final String[] columns = reader.readNext();
    final BeanGenerator generator = new BeanGenerator();
    for (final String column : columns) {
      generator.addProperty(column, String.class);
    }
    this.clazz = (Class<?>) generator.createClass();
    this.properties = new PropertyDescriptor[columns.length];
    for (int i = 0; i < columns.length; i++) {
      try {
        properties[i] = new PropertyDescriptor(columns[i], this.clazz);
      } catch (final IntrospectionException e) {
        LOGGER.warn("Could not build PropertyDescriptor for column {}", columns[i], e);
      }
    }
  }

}
