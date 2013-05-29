package comeon.model.processors;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Processors {
  private static final Logger LOGGER = LoggerFactory.getLogger(Processors.class);
  
  private static final Processors INSTANCE = new Processors();
  
  private final Set<Processor> processors;
  
  private Processors() {
    final Reflections reflections = new Reflections(ClasspathHelper.forPackage("comeon.model.processors"), new SubTypesScanner());
    final Set<Class<? extends Processor>> processorClasses = reflections.getSubTypesOf(Processor.class);
    this.processors = new HashSet<>(processorClasses.size());
    for (final Class<? extends Processor> clazz : processorClasses) {
      try {
        this.processors.add(clazz.newInstance());
      } catch (final InstantiationException | IllegalAccessException e) {
        LOGGER.warn("Can't instantiate {}", clazz, e);
      }
    }
  }
  
  public static Processors getInstance() {
    return INSTANCE;
  }
  
  public Set<Processor> getProcessors() {
    return Collections.unmodifiableSet(processors);
  }
}
