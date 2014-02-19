package comeon.ui.preferences;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;
import javax.swing.SwingConstants;

public final class PreferencesPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private final JTabbedPane tabs;
  
  public PreferencesPanel(final PreferencesController controller) {
    this.tabs = new JTabbedPane(JTabbedPane.TOP);
    this.tabs.add(new ListPanel<>(controller.getWikis()));
    this.tabs.add(new ListPanel<>(controller.getTemplates()));
    this.add(tabs);
  }
  
  private class ListPanel<M> extends JPanel {
    private static final long serialVersionUID = 1L;

    public ListPanel(final ListModel<M> listModel) {
      super(new BorderLayout());
      final JList<M> list = new JList<>(listModel);
      final JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      this.add(scrollPane, BorderLayout.CENTER);
      final JPanel toolbox = new JPanel();
      final GroupLayout toolboxLayout = new GroupLayout(toolbox);
      toolbox.setLayout(toolboxLayout);
      final JButton addButton = new JButton("+");
      final JButton removeButton = new JButton("-");
      final JButton changeButton = new JButton("Δ");
      toolboxLayout.setAutoCreateContainerGaps(true);
      toolboxLayout.setAutoCreateGaps(true);
      toolboxLayout.setHorizontalGroup(toolboxLayout.createParallelGroup(Alignment.CENTER).addComponent(addButton).addComponent(removeButton).addComponent(changeButton));
      toolboxLayout.setVerticalGroup(toolboxLayout.createSequentialGroup().addComponent(addButton).addComponent(removeButton).addComponent(changeButton));
      toolboxLayout.linkSize(SwingConstants.HORIZONTAL, addButton, removeButton, changeButton);
      this.add(toolbox, BorderLayout.EAST);
    }
  }
  
  public static void main(final String... args) {
    final JFrame f = new JFrame();
    final PreferencesController controller = new PreferencesController();
    final PreferencesModel model = new PreferencesModel();
    controller.registerModel(model);
    f.add(new PreferencesPanel(controller));
    f.setSize(800, 600);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
