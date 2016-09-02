package comeon.ui.actions;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.awt.event.ActionEvent;

@Singleton
public final class QuitAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Inject
    public QuitAction() {
        super("quit");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        System.exit(0);
    }

}
