package comeon.core.extmetadata;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;

import comeon.model.Picture;

public final class CsvMetadataSource implements ExternalMetadataSource<Object> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CsvMetadataSource.class);
  
  private HashMap<String, Object> metadata;
  
  private final File metadataFile;

  private final String pictureExpression;
  
  private final String metadataExpression;
  
  public CsvMetadataSource(final String pictureExpression, final String metadataExpression, final File metadataFile) {
    this.pictureExpression = pictureExpression;
    this.metadataExpression = metadataExpression;
    this.metadataFile = metadataFile;
  }
  
  @Override
  public void loadMetadata() {
    final CGLibMappingStrategy strategy = new CGLibMappingStrategy();
    final CsvToBean<Object> csvToBean = new CsvToBean<>();
    //TODO Support setting an encoding
    try (final CSVReader reader = new CSVReader(new FileReader(metadataFile))) {
      final List<Object> beans = csvToBean.parse(strategy, reader);
      this.metadata = new HashMap<>(beans.size());
      for (final Object bean : beans) {
        final String key = String.valueOf(PropertyUtils.getProperty(bean, metadataExpression));
        this.metadata.put(key, bean);
      }
    } catch (final IOException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      LOGGER.error("Could not read metadata file", e);
    }
  }
  
  @Override
  public Object getPictureMetadata(final Picture picture) {
    try {
      final String key = String.valueOf(PropertyUtils.getProperty(picture, pictureExpression));
      return metadata.get(key);
    } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      LOGGER.warn("Can't get property {} from picture", pictureExpression, e);
      return null;
    }
  }
}
