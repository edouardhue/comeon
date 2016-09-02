package comeon.model.processors;

import com.drew.metadata.Directory;

import java.util.Map;

public interface PreProcessor {
    void process(Directory directory, Map<String, Object> metadata);

    Class<? extends Directory> getSupportedClass();
}
