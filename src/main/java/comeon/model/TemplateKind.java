package comeon.model;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comeon.ComeOn;
import comeon.model.processors.PostProcessor;
import comeon.model.processors.Processors;
import comeon.templates.velocity.VelocityTemplates;

public enum TemplateKind {
  VELOCITY {
    @Override
    protected String doRender(final Template template, final String templateText, final Map<String, Object> context) {
      final VelocityEngine engine = VelocityTemplates.getInstance().getEngine(Collections.singletonMap(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, template.getFile().getParent()));
      final Reader templateReader = new StringReader(templateText);
      final Writer outWriter = new StringWriter((int) (templateText.length() * 1.5));
      final Context vContext = new VelocityContext(context);
      final boolean result = engine.evaluate(vContext, outWriter, template.getName(), templateReader);
      if (!result) {
        LOGGER.warn("Velocity template rendering failed, see Velocity log");
      }
      return outWriter.toString();
    }
  };

  private static final Logger LOGGER = LoggerFactory.getLogger(TemplateKind.class);

  public final String render(final Template template, final String templateText, final User user, final Picture picture) {
    final Map<String, Object> context = new HashMap<>();
    for (final PostProcessor processor : Processors.getInstance().getPostProcessors()) {
      processor.process(user, picture, context);
    }
    return this.doRender(template, templateText, context);
  }


  protected abstract String doRender(final Template template, final String templateText, final Map<String, Object> context);
}
