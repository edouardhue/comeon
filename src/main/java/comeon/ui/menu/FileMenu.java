package comeon.ui.menu;

import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.actions.AddPicturesAction;
import comeon.ui.actions.QuitAction;
import comeon.ui.actions.UploadPicturesAction;

@Singleton
public final class FileMenu extends BaseMenu {
  private static final long serialVersionUID = 1L;

  @Inject
  public FileMenu(final AddPicturesAction addPicturesAction, final UploadPicturesAction uploadPicturesAction,
      final QuitAction quitAction) {
    super("file");
    this.add(new JMenuItem(addPicturesAction));
    this.add(new JMenuItem(uploadPicturesAction));
    this.add(new JSeparator());
    this.add(new JMenuItem(quitAction));
  }
}