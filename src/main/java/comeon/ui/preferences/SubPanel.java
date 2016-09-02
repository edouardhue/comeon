package comeon.ui.preferences;

import comeon.ui.UI;
import org.netbeans.validation.api.ui.ValidationGroup;

import javax.swing.*;

public abstract class SubPanel<M> extends JPanel {

    private static final long serialVersionUID = 1L;

    protected static final int COLUMNS = 40;

    private final GroupLayout layout;

    protected SubPanel() {
        layout = new GroupLayout(this);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        this.setLayout(layout);
    }

    protected final void layoutComponents() {
        this.doLayoutComponents(layout);
    }

    protected abstract void doLayoutComponents(final GroupLayout layout);

    public final void attach(final ValidationGroup validationGroup) {
        this.doAttach(validationGroup);
    }

    protected abstract void doAttach(final ValidationGroup validationGroup);

    protected static final class AssociatedLabel extends JLabel {
        private static final long serialVersionUID = 1L;

        public AssociatedLabel(final String key, final JComponent component) {
            super(UI.BUNDLE.getString(key));
            this.setLabelFor(component);
        }
    }
}
