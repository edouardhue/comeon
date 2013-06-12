package comeon.ui.menu;

import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.Core;
import comeon.templates.Templates;
import comeon.ui.actions.AddPicturesAction;
import comeon.ui.actions.QuitAction;
import comeon.ui.actions.UploadPicturesAction;
import comeon.users.Users;

@Singleton
public final class FileMenu extends BaseMenu {
  private static final long serialVersionUID = 1L;

  @Inject
  public FileMenu(final Core core, final Users users, final Templates templates) {
    super("file");
    this.add(new JMenuItem(new AddPicturesAction(users, templates, core)));
    this.add(new JMenuItem(new UploadPicturesAction(core)));
    this.add(new JSeparator());
    this.add(new JMenuItem(new QuitAction()));
  }
}