package comeon.model;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import com.drew.metadata.Directory;
import com.drew.metadata.TagDescriptor;
import comeon.MetadataHelper;
import comeon.templates.velocity.VelocityTemplates;

public enum TemplateKind {
  VELOCITY {
    @Override
    protected String doRender(final String templateText, final Map<String, Object> context) {
      final VelocityEngine engine = VelocityTemplates.getInstance().getEngine();
      final Reader templateReader = new StringReader(templateText);
      final Writer outWriter = new StringWriter((int) (templateText.length() * 1.5));
      final Context vContext = new VelocityContext(context);
      engine.evaluate(vContext, outWriter, "<unnamed>", templateReader);
      return outWriter.toString();
    }
  };
  
  public final String render(final String templateText, final User user, final Picture picture) {
    final Map<String, Object> context = new HashMap<>();
    context.put("picture", picture);
    context.put("user", user);
    for (final Directory dir : picture.getMetadata().getDirectories()) {
        final TagDescriptor<?> descriptor = MetadataHelper.getDescriptor(dir);
        context.put(dir.getName().replaceAll("\\s", ""), descriptor);
    }
    return this.doRender(templateText, context);
  }
  
  protected abstract String doRender(final String templateText, final Map<String, Object> context);
}
