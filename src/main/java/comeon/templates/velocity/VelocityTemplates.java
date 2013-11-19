package comeon.templates.velocity;

import java.util.Map;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.Log4JLogChute;

public final class VelocityTemplates {
  private static final VelocityTemplates INSTANCE = new VelocityTemplates();

  final Properties commonProps;

  private VelocityTemplates() {
    commonProps = new Properties();
    commonProps.put(RuntimeConstants.INPUT_ENCODING, "UTF-8");
    commonProps.put(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
    commonProps.put(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, Log4JLogChute.class.getName());
    commonProps.put("runtime.log.logsystem.log4j.logger", "org.apache.velocity");
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
