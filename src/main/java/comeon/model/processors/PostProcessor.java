package comeon.model.processors;

import java.util.Map;

import comeon.model.Media;
import comeon.model.User;

public interface PostProcessor {
  void process(User user, Media media, Map<String, Object> context);
}
