package comeon.core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;

import comeon.model.Picture;

public final class CsvMetadataSource implements ExternalMetadataSource<DynaBean> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CsvMetadataSource.class);
  
  private HashMap<String, DynaBean> metadata;
  
  private final File metadataFile;

  private final String pictureExpression;
  
  private final String metadataExpression;
  
  public CsvMetadataSource(final String pictureExpression, final String metadataExpression, final File metadataFile) {
    this.pictureExpression = pictureExpression;
    this.metadataExpression = metadataExpression;
    this.metadataFile = metadataFile;
  }
  
  public void readFile() {
    final LazyMappingStrategy strategy = new LazyMappingStrategy();
    final CsvToBean<DynaBean> csvToBean = new CsvToBean<>();
    //TODO Support setting an encoding
    try (final CSVReader reader = new CSVReader(new FileReader(metadataFile))) {
      final List<DynaBean> beans = csvToBean.parse(strategy, reader);
      this.metadata = new HashMap<>(beans.size());
      for (final DynaBean bean : beans) {
        final String key = (String) bean.get(metadataExpression);
        this.metadata.put(key, bean);
      }
    } catch (final IOException e) {
      LOGGER.error("Can't read metadata file", e);
    }
  }
  
  @Override
  public DynaBean getPictureMetadata(final Picture picture) {
    try {
      final String key = String.valueOf(PropertyUtils.getProperty(picture, pictureExpression));
      return metadata.get(key);
    } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      LOGGER.warn("Can't get property {} from picture", pictureExpression, e);
      return null;
    }
  }
}
