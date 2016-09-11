package comeon.ui.toolbar;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.actions.*;

import javax.swing.*;

@Singleton
public final class Toolbar extends JToolBar {

    private static final long serialVersionUID = 1L;

    @Inject
    public Toolbar(final AddMediaAction addMediaAction, final ClearMediaAction clearMediaAction,
                   final UploadMediaAction uploadMediaAction, final PreferencesAction preferencesAction, final HelpManualAction helpManualAction) {
        super(JToolBar.HORIZONTAL);
        this.setFloatable(false);

        this.add(addMediaAction);
        this.add(uploadMediaAction);
        this.add(clearMediaAction);

        this.addSeparator();

        this.add(preferencesAction);

        this.addSeparator();

        this.add(helpManualAction);
    }
}
