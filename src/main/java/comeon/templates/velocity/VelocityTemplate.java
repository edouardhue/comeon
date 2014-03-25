package comeon.templates.velocity;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import comeon.model.Template;
import comeon.model.processors.PostProcessor;
import comeon.templates.BaseTemplateKind;

public final class VelocityTemplate extends BaseTemplateKind {

  private static final Logger LOGGER = LoggerFactory.getLogger(VelocityTemplate.class);
  
  @Inject
  public VelocityTemplate(final Set<PostProcessor> postProcessors) {
    super(postProcessors);
  }
  
  @Override
  protected String doRender(final Template template, final String templateText, final Map<String, Object> context) {
    final VelocityEngine engine = VelocityTemplates.getInstance().getEngine(Collections.singletonMap(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, template.getFile().getParent().toString()));
    final Reader templateReader = new StringReader(templateText);
    final Writer outWriter = new StringWriter((int) (templateText.length() * 1.5));
    final Context vContext = new VelocityContext(context);
    final boolean result = engine.evaluate(vContext, outWriter, template.getName(), templateReader);
    if (!result) {
      LOGGER.warn("Velocity template rendering failed, see Velocity log");
    }
    return outWriter.toString();
  }
  
  public String toString() {
    return "Velocity";
  }

}
