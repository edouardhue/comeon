package comeon.templates.velocity;

import org.apache.velocity.app.VelocityEngine;

public final class VelocityTemplates {
  private static final VelocityTemplates INSTANCE = new VelocityTemplates();

  private final VelocityEngine engine;
  
  private VelocityTemplates() {
    engine = new VelocityEngine();
    engine.init();
  }
  
  public static VelocityTemplates getInstance() {
    return INSTANCE;
  }
  
  public VelocityEngine getEngine() {
    return engine;
  }
}
