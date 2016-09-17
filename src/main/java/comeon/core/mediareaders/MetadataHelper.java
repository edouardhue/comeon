package comeon.core.mediareaders;

import com.drew.metadata.Directory;
import com.drew.metadata.TagDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

final class MetadataHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataHelper.class);

    private MetadataHelper() {
        super();
    }

    @SuppressWarnings("unchecked")
    static <T extends Directory> TagDescriptor<T> getDescriptor(final T dir) {
        final String dirClassName = dir.getClass().getName();
        final String descriptorClassName = dirClassName.replace("Directory", "Descriptor");
        try {
            final Class<TagDescriptor<T>> descriptorClass = (Class<TagDescriptor<T>>) Class.forName(descriptorClassName);
            final TagDescriptor<T> descriptor = descriptorClass.getDeclaredConstructor(dir.getClass()).newInstance(dir);
            return descriptor;
        } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            LOGGER.warn("Can't instantiate tag descriptor for directory {}", dir.getName(), e);
            throw new RuntimeException("Can't instantiate a descriptor for metadata directory", e);
        }
    }
}
