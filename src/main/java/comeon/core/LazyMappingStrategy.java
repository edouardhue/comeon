package comeon.core;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.MappingStrategy;

public final class LazyMappingStrategy implements MappingStrategy<DynaBean> {
  private static final Logger LOGGER = LoggerFactory.getLogger(LazyMappingStrategy.class);
  
  private LazyDynaClass clazz;
  
  private PropertyDescriptor[] descriptors;
  
  @Override
  public PropertyDescriptor findDescriptor(final int col) throws IntrospectionException {
    return descriptors[col];
  }

  @Override
  public DynaBean createBean() throws InstantiationException, IllegalAccessException {
    return clazz.newInstance();
  }

  @Override
  public void captureHeader(final CSVReader reader) throws IOException {
    final String[] header = reader.readNext();
    this.clazz = new LazyDynaClass();
    for (int i = 0; i < header.length; i++) {
      final String column = header[i];
      this.clazz.add(column, String.class);
      try {
        descriptors[i] = new PropertyDescriptor(column, clazz.getDynaBeanClass());
      } catch (final IntrospectionException e) {
        LOGGER.error("Can't build property descriptor", e);
      }
    }
  }


}
