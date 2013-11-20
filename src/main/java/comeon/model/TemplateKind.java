package comeon.model;


public interface TemplateKind {
  String render(Template template, String templateText, User user, Picture picture);
}
