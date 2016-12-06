package comeon.ui.add;

import comeon.core.Core;
import comeon.ui.CursorChangingWorker;
import comeon.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;

public class AdderWorker extends CursorChangingWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdderWorker.class);

    private final AddModel model;

    private final Core core;

    public AdderWorker(final AddModel model, final Core core) {
        this.model = model;
        this.core = core;
    }

    @Override
    protected Void doInBackground() throws Exception {
        model.getTemplate().load();
        final File[] files = model.getMediaFiles();
        if (files.length > 0) {
            core.addMedia(files, model.getTemplate(), model.getExternalMetadataSource());
        }
        return null;
    }

    @Override
    protected void done() {
        super.done();
        try {
            super.get();
        } catch (final ExecutionException e) {
            LOGGER.warn("Adding media failed", e);
            final Window window = UI.findInstance();
            JOptionPane.showMessageDialog(
                    window,
                    MessageFormat.format(UI.BUNDLE.getString("error.addmedia.failed"), e.getCause().getLocalizedMessage()),
                    UI.BUNDLE.getString("error.generic.title"),
                    JOptionPane.ERROR_MESSAGE);
        } catch (final InterruptedException e) {
            Thread.interrupted();
        }
    }
}
