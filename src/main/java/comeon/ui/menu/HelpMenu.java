package comeon.ui.menu;

import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.actions.AboutAction;
import comeon.ui.actions.HelpManualAction;

@Singleton
public final class HelpMenu extends BaseMenu {
  private static final long serialVersionUID = 1L;

  @Inject
  public HelpMenu() {
    super("help");
    this.add(new JMenuItem(new HelpManualAction()));
    this.add(new JSeparator());
    this.add(new JMenuItem(new AboutAction()));
  }
}