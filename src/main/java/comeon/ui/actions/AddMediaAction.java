package comeon.ui.actions;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.Core;
import comeon.templates.Templates;
import comeon.templates.TemplatesChangedEvent;
import comeon.ui.add.AddMediaDialog;
import comeon.ui.add.AddModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Optional;

@Singleton
public final class AddMediaAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    private final Templates templates;

    private final Core core;

    @Inject
    public AddMediaAction(final Templates templates, final Core core) {
        super("addmedia");
        this.templates = templates;
        this.core = core;
        if (templates.getTemplates().isEmpty()) {
            this.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // Ancestor may be null in presence of keywtrokes
        final Optional<Window> window = Optional.ofNullable(SwingUtilities.getWindowAncestor((Component) e.getSource()));
        SwingUtilities.invokeLater(() -> {
            final AddMediaDialog dialog = new AddMediaDialog(templates);
            final int value = dialog.showDialog();
            if (value == JOptionPane.OK_OPTION) {
                window.ifPresent(w -> w.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)));
                final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        final AddModel model = dialog.getModel();
                        final File[] files = model.getMediaFiles();
                        if (files.length > 0) {
                            core.addMedia(files, model.getTemplate(), model.getExternalMetadataSource());
                        }
                        return null;
                    }
                };
                worker.addPropertyChangeListener(evt -> {
                    if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE.equals(evt.getNewValue())) {
                        SwingUtilities.invokeLater(() -> window.ifPresent(w -> w.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))));
                    }
                });
                worker.execute();
            }
        });
    }

    @Subscribe
    public void handleTemplatesChanged(final TemplatesChangedEvent event) {
        this.setEnabled(!event.getTemplates().isEmpty());
    }
}
