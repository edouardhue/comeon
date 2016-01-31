package comeon.model.processors;

import java.util.Map;

import comeon.model.Media;
import comeon.model.User;

public final class DefaultPostProcessor implements PostProcessor {
  private static final String MEDIA = "media";

  private static final String USER = "user";

  @Override
  public void process(final User user, final Media media, final Map<String, Object> context) {
    context.put(MEDIA, media);
    context.put(USER, user);
    for (final Map.Entry<String, Object> entry : media.getMetadata().entrySet()) {
      final String directoryName = entry.getKey().replaceAll("\\s", "");
      context.put(directoryName, entry.getValue());
    }
  }

}
