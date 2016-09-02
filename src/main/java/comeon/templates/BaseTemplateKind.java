package comeon.templates;

import comeon.model.Media;
import comeon.model.Template;
import comeon.model.TemplateKind;
import comeon.model.User;
import comeon.model.processors.PostProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseTemplateKind implements TemplateKind {

    private final Set<PostProcessor> postProcessors;

    protected BaseTemplateKind(final Set<PostProcessor> postProcessors) {
        this.postProcessors = postProcessors;
    }

    @Override
    public final String render(final Template template, final String templateText, final User user, final Media media) {
        final Map<String, Object> context = new HashMap<>();
        for (final PostProcessor processor : postProcessors) {
            processor.process(user, media, context);
        }
        return this.doRender(template, templateText, context);
    }

    protected abstract String doRender(Template template, String templateText, Map<String, Object> context);
}
