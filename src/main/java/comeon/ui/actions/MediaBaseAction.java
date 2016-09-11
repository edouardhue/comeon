package comeon.ui.actions;

import com.google.common.eventbus.Subscribe;
import comeon.core.Core;

import javax.swing.*;

abstract class MediaBaseAction extends BaseAction {

    protected final Core core;

    protected MediaBaseAction(final String key, final Core core) {
        super(key);
        this.core = core;
    }

    protected MediaBaseAction(final String key, final KeyStroke accelerator, final Core core) {
        super(key, accelerator);
        this.core = core;
    }

    @Subscribe
    public void handleMediaAddedEvent(final MediaAddedEvent event) {
        this.enableIfMediaAreAvailable();
    }

    @Subscribe
    public void handleMediaRemovedEvent(final MediaRemovedEvent event) {
        this.enableIfMediaAreAvailable();
    }

    private void enableIfMediaAreAvailable() {
        SwingUtilities.invokeLater(() -> setEnabled(core.countMediaToBeUploaded() > 0));
    }
}
