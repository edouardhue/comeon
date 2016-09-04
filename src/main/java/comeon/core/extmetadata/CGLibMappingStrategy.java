package comeon.core.extmetadata;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.MappingStrategy;
import net.sf.cglib.beans.BeanGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

final class CGLibMappingStrategy implements MappingStrategy<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CGLibMappingStrategy.class);

    private Class<?> clazz;

    private final List<Optional<PropertyDescriptor>> properties;

    public CGLibMappingStrategy() {
        this.properties = new LinkedList<>();
    }

    @Override
    public PropertyDescriptor findDescriptor(final int col) throws IntrospectionException {
        return properties.get(col).orElseThrow(() -> new IntrospectionException(String.format("Missing descriptor for column %1$s", col)));
    }

    @Override
    public Object createBean() throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

    @Override
    public void captureHeader(final CSVReader reader) throws IOException {
        final String[] columns = reader.readNext();
        properties.clear();
        final BeanGenerator generator = new BeanGenerator();
        Arrays.stream(columns).forEach(c -> generator.addProperty(c, String.class));
        this.clazz = (Class<?>) generator.createClass();
        this.properties.addAll(Arrays.stream(columns).map(this::buildPropertyDescriptor).collect(Collectors.toList()));
    }

    private Optional<PropertyDescriptor> buildPropertyDescriptor(final String column) {
        try {
            return Optional.of(new PropertyDescriptor(column, clazz));
        } catch (final IntrospectionException e) {
            LOGGER.warn("Could not build PropertyDescriptor for column {}", column, e);
            return Optional.empty();
        }
    }

}
