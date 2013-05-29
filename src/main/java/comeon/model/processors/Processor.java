package comeon.model.processors;

import java.util.Map;

import comeon.model.Picture;
import comeon.model.User;

public interface Processor {
  void process(User user, Picture picture, Map<String, Object> context);
}
