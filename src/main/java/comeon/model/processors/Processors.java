package comeon.model.processors;

import java.util.Collections;
import java.util.Set;

import com.drew.metadata.Directory;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public final class Processors {
  @Inject
  private static Processors INSTANCE;

  private final Set<PreProcessor> preProcessors;

  private final Set<PostProcessor> postProcessors;
  
  @Inject
  private Processors(final Set<PreProcessor> preProcessors, final Set<PostProcessor> postProcessors) {
    this.preProcessors = preProcessors;
    this.postProcessors = postProcessors;
  }

  public static Processors getInstance() {
    return INSTANCE;
  }

  @Deprecated
  public Set<PostProcessor> getPostProcessors() {
    return Collections.unmodifiableSet(postProcessors);
  }
}
