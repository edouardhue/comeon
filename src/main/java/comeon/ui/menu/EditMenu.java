package comeon.ui.menu;

import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.templates.Templates;
import comeon.ui.actions.PreferencesAction;
import comeon.wikis.Wikis;

@Singleton
public final class EditMenu extends BaseMenu {
  private static final long serialVersionUID = 1L;

  @Inject
  public EditMenu(final Templates templates, final Wikis wikis) {
    super("edit");
    this.add(new JMenuItem(new PreferencesAction(templates, wikis)));
  }
}