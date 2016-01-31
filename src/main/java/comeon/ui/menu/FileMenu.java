package comeon.ui.menu;

import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.actions.AddMediaAction;
import comeon.ui.actions.QuitAction;
import comeon.ui.actions.UploadMediaAction;

@Singleton
public final class FileMenu extends BaseMenu {
  private static final long serialVersionUID = 1L;

  @Inject
  public FileMenu(final AddMediaAction addMediaAction, final UploadMediaAction uploadMediaAction,
      final QuitAction quitAction) {
    super("file");
    this.add(new JMenuItem(addMediaAction));
    this.add(new JMenuItem(uploadMediaAction));
    this.add(new JSeparator());
    this.add(new JMenuItem(quitAction));
  }
}