package comeon.templates;

import java.util.List;
import java.util.prefs.BackingStoreException;

import comeon.model.Template;

public interface Templates {

  List<Template> getTemplates();

  void setTemplates(List<Template> templates);

  void save() throws BackingStoreException;

}