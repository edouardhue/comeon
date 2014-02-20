package comeon.ui.preferences;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;
import javax.swing.SwingConstants;

import comeon.ui.UI;

public final class PreferencesPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private final ListPanel<TemplateModel> templatesPanel;
  
  private final ListPanel<WikiModel> wikisPanel;
  
  public PreferencesPanel() {
    this.templatesPanel = new ListPanel<>(new TemplateListCellRenderer(), TemplateModel.getPrototype());
    this.wikisPanel = new ListPanel<>(new WikiListCellRenderer(), WikiModel.getPrototype());
    final JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
    tabs.add(UI.BUNDLE.getString("prefs.tab.templates"), templatesPanel);
    tabs.add(UI.BUNDLE.getString("prefs.tab.wikis"), wikisPanel);
    this.add(tabs);
  }
  
  void updateModels(final ListModel<TemplateModel> templatesModel, final ListModel<WikiModel> wikisModel) {
    this.templatesPanel.updateModel(templatesModel);
    this.wikisPanel.updateModel(wikisModel);
  }
  
  private class ListPanel<M> extends JPanel {
    private static final long serialVersionUID = 1L;

    private final JList<M> list;
    
    public ListPanel(final BaseListCellRenderer<M> renderer, final M prototypeValue) {
      super();
      
      this.list = new JList<>();
      this.list.setCellRenderer(renderer);
      this.list.setPrototypeCellValue(prototypeValue);
      final JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      final JPanel toolbox = buildToolbox();
      
      final GroupLayout layout = new GroupLayout(this);
      this.setLayout(layout);
      layout.setAutoCreateContainerGaps(true);
      layout.setAutoCreateGaps(true);
      layout.setHorizontalGroup(layout.createSequentialGroup()
          .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
          .addComponent(toolbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(scrollPane).addComponent(toolbox));
    }

    private JPanel buildToolbox() {
      final JPanel toolbox = new JPanel();
      toolbox.setBorder(BorderFactory.createEtchedBorder());
      final GroupLayout toolboxLayout = new GroupLayout(toolbox);
      toolbox.setLayout(toolboxLayout);
      final JButton addButton = new JButton(UI.BUNDLE.getString("prefs.add"));
      final JButton removeButton = new JButton(UI.BUNDLE.getString("prefs.remove"));
      final JButton changeButton = new JButton(UI.BUNDLE.getString("prefs.change"));
      toolboxLayout.setAutoCreateContainerGaps(true);
      toolboxLayout.setAutoCreateGaps(true);
      toolboxLayout.setHorizontalGroup(toolboxLayout.createParallelGroup(Alignment.CENTER).addComponent(addButton).addComponent(removeButton).addComponent(changeButton));
      toolboxLayout.setVerticalGroup(toolboxLayout.createSequentialGroup().addComponent(addButton).addComponent(removeButton).addComponent(changeButton));
      toolboxLayout.linkSize(SwingConstants.HORIZONTAL, addButton, removeButton, changeButton);
      return toolbox;
    }

    private void updateModel(final ListModel<M> model) {
      this.list.setModel(model);
    }
  }
}
