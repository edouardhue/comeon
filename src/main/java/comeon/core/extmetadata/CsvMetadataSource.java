package comeon.core.extmetadata;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import comeon.model.Media;
import org.apache.commons.beanutils.LazyDynaMap;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CsvMetadataSource implements ExternalMetadataSource<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvMetadataSource.class);

    private final HashMap<String, Object> metadata;

    private final Path metadataFile;

    private final String mediaExpression;

    private final String metadataExpression;

    private final char separator;

    private final char quote;

    private final char escape;

    private final int skipLines;

    private final boolean strictQuotes;

    private final boolean ignoreLeadingWhiteSpace;

    private final Charset charset;

    private final KeyTransformer keyTransformer;

    public CsvMetadataSource(final String mediaExpression, final String metadataExpression, final Path metadataFile, final char separator, final char quote, final char escape,
                             final int skipLines, final boolean strictQuotes, final boolean ignoreLeadingWhiteSpace, final Charset charset, final KeyTransformer keyTransformer) {
        this.mediaExpression = mediaExpression;
        this.metadataExpression = metadataExpression;
        this.metadataFile = metadataFile;
        this.metadata = new HashMap<>();
        this.separator = separator;
        this.quote = quote;
        this.escape = escape;
        this.skipLines = skipLines;
        this.strictQuotes = strictQuotes;
        this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
        this.charset = charset;
        this.keyTransformer = keyTransformer;
    }

    @Override
    public void loadMetadata() {
        final CGLibMappingStrategy strategy = new CGLibMappingStrategy();
        final CsvToBean<Object> csvToBean = new CsvToBean<>();
        this.metadata.clear();
        try (final CSVReader reader = new CSVReader(Files.newBufferedReader(metadataFile, charset), separator, quote, escape, skipLines, strictQuotes, ignoreLeadingWhiteSpace)) {
            final List<Object> beans = csvToBean.parse(strategy, reader);
            this.metadata.putAll(beans.parallelStream().collect(Collectors.toConcurrentMap(
                    bean -> {
                        try {
                            return String.valueOf(PropertyUtils.getProperty(bean, metadataExpression));
                        } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            LOGGER.error("Could not read property {} from {}", metadataExpression, bean);
                            return null;
                        }
                    },
                    Function.identity()
            )));
        } catch (final IOException e) {
            LOGGER.error("Could not read metadata file", e);
        }
    }

    @Override
    public Object getMediaMetadata(final Media media, final Map<String, Object> mediaMetadata) {
        try {
            final LazyDynaMap bean = new LazyDynaMap(mediaMetadata);
            final String key = String.valueOf(PropertyUtils.getNestedProperty(bean, mediaExpression));
            return metadata.get(keyTransformer.transform(key));
        } catch (final IllegalAccessException | InvocationTargetException | NestedNullException | NoSuchMethodException e) {
            try {
                final String key = String.valueOf(PropertyUtils.getNestedProperty(media, mediaExpression));
                return metadata.get(keyTransformer.transform(key));
            } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e2) {
                LOGGER.warn("Can't get property {} from media", mediaExpression, e2);
                return null;
            }
        }
    }
}
