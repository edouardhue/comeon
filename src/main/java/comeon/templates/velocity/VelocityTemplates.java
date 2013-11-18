package comeon.templates.velocity;

import java.util.Map;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;

public final class VelocityTemplates {
  private static final VelocityTemplates INSTANCE = new VelocityTemplates();

  final Properties commonProps;

  private VelocityTemplates() {
    commonProps = new Properties();
    commonProps.put("input.encoding", "UTF-8");
    commonProps.put("output.encoding", "UTF-8");
  }

  public static VelocityTemplates getInstance() {
    return INSTANCE;
  }

  public VelocityEngine getEngine(final Map<String, String> additionalProps) {
    final VelocityEngine engine = new VelocityEngine();
    final Properties engineProps = new Properties(commonProps);
    engineProps.putAll(additionalProps);
    engine.init(engineProps);
    return engine;
  }
}
