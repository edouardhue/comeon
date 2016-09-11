package comeon.ui.menu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.actions.AddMediaAction;
import comeon.ui.actions.ClearMediaAction;
import comeon.ui.actions.QuitAction;
import comeon.ui.actions.UploadMediaAction;

import javax.swing.*;

@Singleton
public final class FileMenu extends BaseMenu {
    private static final long serialVersionUID = 1L;

    @Inject
    public FileMenu(final AddMediaAction addMediaAction, final ClearMediaAction clearMediaAction,
                    final UploadMediaAction uploadMediaAction, final QuitAction quitAction) {
        super("file");
        this.add(new JMenuItem(addMediaAction));
        this.add(new JMenuItem(uploadMediaAction));
        this.add(new JMenuItem(clearMediaAction));
        this.add(new JSeparator());
        this.add(new JMenuItem(quitAction));
    }
}