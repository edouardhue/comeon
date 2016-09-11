package comeon.ui.preferences.main;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.actions.BaseAction;
import comeon.ui.preferences.wikis.WikiListCellRenderer;
import comeon.ui.preferences.wikis.WikiModel;
import comeon.ui.preferences.wikis.WikiSubController;
import comeon.ui.preferences.wikis.WikiSubPanel;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;

@Singleton
public final class WikisListPanel extends ListPanel<WikiModel> {
    private static final long serialVersionUID = 1L;

    private final ActivateAction activateAction;

    @Inject
    public WikisListPanel(final WikiSubController subController,
                          final WikiSubPanel subPanel) {
        super(new WikiListCellRenderer(), subController, subPanel, subController.getMainController().getWikis(),
                "wikis", new ImageIcon(Resources.getResource("comeon/ui/wiki_large.png")), WikiModel.getPrototype());
        this.activateAction = new ActivateAction();
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                final boolean isSomethingSelected = list.getSelectedIndex() >= 0;
                SwingUtilities.invokeLater(() -> activateAction.setEnabled(isSomethingSelected));
            }
        });
        list.getModel().addListDataListener(new ListDataListener() {
            @Override
            public void intervalRemoved(final ListDataEvent e) {
                this.updateActivateActionStatus();
            }

            @Override
            public void intervalAdded(final ListDataEvent e) {
                this.updateActivateActionStatus();
            }

            @Override
            public void contentsChanged(final ListDataEvent e) {
                // Noop
            }

            private void updateActivateActionStatus() {
                SwingUtilities.invokeLater(() -> {
                    activateAction.setEnabled(list.getModel().getSize() > 1);
                    if (list.getModel().getSize() == 1) {
                        subController.getMainController().setActiveWiki(0);
                    }
                });
            }
        });
        super.addCustomButton(new JButton(activateAction));
    }

    private class ActivateAction extends BaseAction {
        private static final long serialVersionUID = 1L;

        public ActivateAction() {
            super("prefs.wikis.activate");
            this.setEnabled(false);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            subController.getMainController().setActiveWiki(list.getSelectedIndex());
        }

    }
}