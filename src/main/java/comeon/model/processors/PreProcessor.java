package comeon.model.processors;

import java.util.Map;

import com.drew.metadata.Directory;

public interface PreProcessor {
  void process(Directory directory, Map<String, Object> metadata);
  Class<? extends Directory> getSupportedClass();
}
