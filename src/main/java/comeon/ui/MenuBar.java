package comeon.ui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import comeon.core.Core;
import comeon.templates.Templates;
import comeon.ui.actions.AboutAction;
import comeon.ui.actions.AddPicturesAction;
import comeon.ui.actions.HelpManualAction;
import comeon.ui.actions.PreferencesAction;
import comeon.ui.actions.QuitAction;
import comeon.ui.actions.UploadPicturesAction;
import comeon.users.Users;

final class MenuBar extends JMenuBar {

  private static final long serialVersionUID = 1L;

  public MenuBar(final UI ui, final Core core, final Users users, final Templates templates) {
    this.add(new FileMenu(ui, core, users, templates));
    this.add(new EditMenu(ui, users, templates));
    this.add(new HelpMenu(ui));
  }

  protected abstract class BaseMenu extends JMenu {

    private static final long serialVersionUID = 1L;
    
    private BaseMenu(final String bundleKey, final UI ui) {
      super(UI.BUNDLE.getString("menu." + bundleKey + ".title"));
      final char mnemo = UI.BUNDLE.getString("menu." + bundleKey + ".mnemo").charAt(0);
      this.setMnemonic(mnemo);
    }
    
  }
  
  private class FileMenu extends BaseMenu {
    private static final long serialVersionUID = 1L;

    public FileMenu(final UI ui, final Core core, final Users users, final Templates templates) {
      super("file", ui);
      this.add(new JMenuItem(new AddPicturesAction(ui, users, templates, core)));
      this.add(new JMenuItem(new UploadPicturesAction(ui, core)));
      this.add(new JSeparator());
      this.add(new JMenuItem(new QuitAction(ui)));
    }
  }

  private class EditMenu extends BaseMenu {
    private static final long serialVersionUID = 1L;

    public EditMenu(final UI ui, final Users users, final Templates templates) {
      super("edit", ui);
      this.add(new JMenuItem(new PreferencesAction(ui, users, templates)));
    }
  }

  private class HelpMenu extends BaseMenu {
    private static final long serialVersionUID = 1L;

    public HelpMenu(final UI ui) {
      super("help", ui);
      this.add(new JMenuItem(new HelpManualAction(ui)));
      this.add(new JSeparator());
      this.add(new JMenuItem(new AboutAction(ui)));
    }
  }
}
