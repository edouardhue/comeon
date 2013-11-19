package comeon.model.processors;

import java.util.Map;

import comeon.model.Picture;
import comeon.model.User;

public final class DefaultPostProcessor implements PostProcessor {
  private static final String PICTURE = "picture";

  private static final String USER = "user";

  @Override
  public void process(final User user, final Picture picture, final Map<String, Object> context) {
    context.put(PICTURE, picture);
    context.put(USER, user);
    for (final Map.Entry<String, Object> entry : picture.getMetadata().entrySet()) {
      final String directoryName = entry.getKey().replaceAll("\\s", "");
      context.put(directoryName, entry.getValue());
    }
  }

}
