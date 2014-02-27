package comeon.ui.preferences;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import comeon.ui.UI;

abstract class SubPanel<M> extends JPanel {

  private static final long serialVersionUID = 1L;

  private final GroupLayout layout;
  
  protected SubPanel() {
    this.layout = new GroupLayout(this);
    layout.setAutoCreateContainerGaps(true);
    layout.setAutoCreateGaps(true);
    this.setLayout(layout);
  }
  
  protected final void layoutComponents() {
    this.doLayoutComponents(layout);
  }
  
  protected abstract void doLayoutComponents(final GroupLayout layout);
  
  protected static final class AssociatedLabel extends JLabel {
    private static final long serialVersionUID = 1L;

    public AssociatedLabel(final String key, final JComponent component) {
      super(UI.BUNDLE.getString(key));
      this.setLabelFor(component);
    }
  }
}
