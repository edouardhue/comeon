package comeon.ui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import comeon.ui.actions.AboutAction;
import comeon.ui.actions.AddPicturesAction;
import comeon.ui.actions.HelpManualAction;
import comeon.ui.actions.PreferencesAction;
import comeon.ui.actions.QuitAction;
import comeon.ui.actions.UploadPicturesAction;

final class MenuBar extends JMenuBar {

  private static final long serialVersionUID = 1L;

  public MenuBar() {
    this.add(new FileMenu());
    this.add(new EditMenu());
    this.add(new HelpMenu());
  }

  protected abstract class BaseMenu extends JMenu {

    private static final long serialVersionUID = 1L;
    
    private BaseMenu(final String bundleKey) {
      super(UI.BUNDLE.getString("menu." + bundleKey + ".title"));
      final char mnemo = UI.BUNDLE.getString("menu." + bundleKey + ".mnemo").charAt(0);
      this.setMnemonic(mnemo);
    }
    
  }
  
  private class FileMenu extends BaseMenu {
    private static final long serialVersionUID = 1L;

    public FileMenu() {
      super("file");
      this.add(new JMenuItem(new AddPicturesAction()));
      this.add(new JMenuItem(new UploadPicturesAction()));
      this.add(new JSeparator());
      this.add(new JMenuItem(new QuitAction()));
    }
  }

  private class EditMenu extends BaseMenu {
    private static final long serialVersionUID = 1L;

    public EditMenu() {
      super("edit");
      this.add(new JMenuItem(new PreferencesAction()));
    }
  }

  private class HelpMenu extends BaseMenu {
    private static final long serialVersionUID = 1L;

    public HelpMenu() {
      super("help");
      this.add(new JMenuItem(new HelpManualAction()));
      this.add(new JSeparator());
      this.add(new JMenuItem(new AboutAction()));
    }
  }
}
