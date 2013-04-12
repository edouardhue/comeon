package comeon.templates.velocity;

import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;

public final class VelocityTemplates {
  private static final String VELOCITY_TEMPLATE_ROOT_KEY = "velocity.templateRoot";

  private static final VelocityTemplates INSTANCE = new VelocityTemplates();

  private final VelocityEngine engine;
  
  private VelocityTemplates() {
    engine = new VelocityEngine();
    if (System.getProperties().containsKey(VELOCITY_TEMPLATE_ROOT_KEY)) {
      final Properties veloProps = new Properties();
      veloProps.put("file.resource.loader.path", System.getProperty(VELOCITY_TEMPLATE_ROOT_KEY));
      veloProps.put("input.encoding", "UTF-8");
      veloProps.put("output.encoding", "UTF-8");
      engine.init(veloProps);
    } else {
      engine.init();
    }
  }
  
  public static VelocityTemplates getInstance() {
    return INSTANCE;
  }
  
  public VelocityEngine getEngine() {
    return engine;
  }
}
