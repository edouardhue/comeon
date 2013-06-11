package comeon.model.processors;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.metadata.Directory;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

public final class Processors {
  private static final Logger LOGGER = LoggerFactory.getLogger(Processors.class);
  
  private static final Processors INSTANCE = new Processors();
  
  private final Set<PreProcessor> preProcessors;

  private final Set<PostProcessor> postProcessors;
  
  private Processors() {
    final Reflections reflections = new Reflections(ClasspathHelper.forPackage("comeon.model.processors"), new SubTypesScanner());
    
    final Set<Class<? extends PostProcessor>> postProcessorClasses = reflections.getSubTypesOf(PostProcessor.class);
    this.postProcessors = new HashSet<>(postProcessorClasses.size());
    for (final Class<? extends PostProcessor> clazz : postProcessorClasses) {
      try {
        this.postProcessors.add(clazz.newInstance());
      } catch (final InstantiationException | IllegalAccessException e) {
        LOGGER.warn("Can't instantiate {}", clazz, e);
      }
    }

    final Set<Class<? extends PreProcessor>> preProcessorClasses = reflections.getSubTypesOf(PreProcessor.class);
    this.preProcessors = new HashSet<>(preProcessorClasses.size());
    for (final Class<? extends PreProcessor> clazz : preProcessorClasses) {
      try {
        this.preProcessors.add(clazz.newInstance());
      } catch (final InstantiationException | IllegalAccessException e) {
        LOGGER.warn("Can't instantiate {}", clazz, e);
      }
    }
  }
  
  public static Processors getInstance() {
    return INSTANCE;
  }
  
  public Set<PreProcessor> getPreProcessors(final Class<? extends Directory> clazz) {
    final Predicate<PreProcessor> predicate = new Predicate<PreProcessor>() {
      public boolean apply(final PreProcessor processor) {
        return clazz.isAssignableFrom(processor.getSupportedClass());
      }
    };
    return Sets.filter(this.preProcessors, predicate);
  }
  
  public Set<PostProcessor> getPostProcessors() {
    return Collections.unmodifiableSet(postProcessors);
  }
}
