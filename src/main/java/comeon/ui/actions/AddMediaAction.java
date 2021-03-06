package comeon.ui.actions;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.Core;
import comeon.templates.Templates;
import comeon.templates.TemplatesChangedEvent;
import comeon.ui.add.AddMediaDialog;
import comeon.ui.add.AdderWorker;

import javax.swing.*;
import java.awt.event.ActionEvent;

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
        SwingUtilities.invokeLater(() -> {
            final AddMediaDialog dialog = new AddMediaDialog(templates);
            final int value = dialog.showDialog();
            if (value == JOptionPane.OK_OPTION) {
                new AdderWorker(dialog.getModel(), core).execute();
            }
        });
    }

    @Subscribe
    public void handleTemplatesChanged(final TemplatesChangedEvent event) {
        this.setEnabled(!event.getTemplates().isEmpty());
    }

}
