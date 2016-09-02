package comeon.model.processors;

import comeon.model.Media;
import comeon.model.User;

import java.util.Map;

public interface PostProcessor {
    void process(User user, Media media, Map<String, Object> context);
}
