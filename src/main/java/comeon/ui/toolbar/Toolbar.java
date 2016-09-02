package comeon.ui.toolbar;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.actions.AddMediaAction;
import comeon.ui.actions.HelpManualAction;
import comeon.ui.actions.PreferencesAction;
import comeon.ui.actions.UploadMediaAction;

import javax.swing.*;

@Singleton
public final class Toolbar extends JToolBar {

    private static final long serialVersionUID = 1L;

    @Inject
    public Toolbar(final AddMediaAction addMediaAction, final UploadMediaAction uploadMediaAction, final PreferencesAction preferencesAction, final HelpManualAction helpManualAction) {
        super(JToolBar.HORIZONTAL);
        this.setFloatable(false);

        this.add(addMediaAction);
        this.add(uploadMediaAction);

        this.addSeparator();

        this.add(preferencesAction);

        this.addSeparator();

        this.add(helpManualAction);
    }
}
