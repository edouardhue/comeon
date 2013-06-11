package comeon.templates.velocity;

import java.util.List;
import java.util.prefs.BackingStoreException;

import comeon.model.Template;

public interface Templates {

  public abstract void readPreferences() throws BackingStoreException;

  public abstract List<Template> getTemplates();

  public abstract void setTemplates(List<Template> templates);

  public abstract void storePreferences() throws BackingStoreException;

}