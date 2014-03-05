package comeon.ui.preferences;

import java.awt.event.ActionEvent;
import java.nio.charset.Charset;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import comeon.model.TemplateKind;
import comeon.ui.UI;

public final class PreferencesPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private final ListPanel<TemplateModel> templatesPanel;
  
  private final ListPanel<WikiModel> wikisPanel;
  
  private final TemplateSubPanel templateSubPanel;
  
  private final WikiSubPanel wikiSubPanel;
  
  public PreferencesPanel(final Charset[] charsets, final TemplateKind[] kinds) {
    this.templateSubPanel = new TemplateSubPanel(charsets, kinds);
    this.wikiSubPanel = new WikiSubPanel();

    this.templatesPanel = new ListPanel<>(new TemplateListCellRenderer(), this.templateSubPanel, "templates", TemplateModel.getPrototype());
    this.wikisPanel = new ListPanel<>(new WikiListCellRenderer(), this.wikiSubPanel, "wikis", WikiModel.getPrototype());
    
    final JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
    tabs.add(UI.BUNDLE.getString("prefs.tab.templates"), templatesPanel);
    tabs.add(UI.BUNDLE.getString("prefs.tab.wikis"), wikisPanel);
    this.add(tabs);
  }

  TemplateSubPanel getTemplateSubPanel() {
    return templateSubPanel;
  }
  
  WikiSubPanel getWikiSubPanel() {
    return wikiSubPanel;
  }
  
  void setController(final PreferencesController controller) {
    this.templatesPanel.setSubController(controller.getTemplateSubController());
    this.wikisPanel.setSubController(controller.getWikiSubController());
  }
  
  void updateModels(final ListModel<TemplateModel> templatesModel, final ListModel<WikiModel> wikisModel) {
    this.templatesPanel.updateModel(templatesModel);
    this.wikisPanel.updateModel(wikisModel);
  }
  
  private class ListPanel<M extends Model> extends JPanel {
    private static final long serialVersionUID = 1L;

    private final JList<M> list;
    
    private final AddAction addAction;
    
    private final RemoveAction removeAction;
    
    private final ChangeAction changeAction;
    
    private final SubPanel<M> subPanel;
    
    private SubController<M, ? extends SubPanel<M>> subController;
    
    public ListPanel(final BaseListCellRenderer<M> renderer, final SubPanel<M> subPanel, final String stringsKey, final M prototypeValue) {
      super();

      this.subPanel = subPanel;
      
      this.addAction = new AddAction(stringsKey);
      this.removeAction = new RemoveAction(stringsKey);
      this.changeAction = new ChangeAction(stringsKey);
      
      this.list = new JList<>();
      this.list.setCellRenderer(renderer);
      this.list.setPrototypeCellValue(prototypeValue);
      this.list.addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(final ListSelectionEvent e) {
          if (!e.getValueIsAdjusting()) {
            final boolean isSomethingSelected = e.getFirstIndex() >= 0;
            SwingUtilities.invokeLater(new Runnable() {
              @Override
              public void run() {
                removeAction.setEnabled(isSomethingSelected);
                changeAction.setEnabled(isSomethingSelected);
              }
            });
          }
        }
      });
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
      final JButton addButton = new JButton(addAction);
      final JButton removeButton = new JButton(removeAction);
      final JButton changeButton = new JButton(changeAction);
      toolboxLayout.setAutoCreateContainerGaps(true);
      toolboxLayout.setAutoCreateGaps(true);
      toolboxLayout.setHorizontalGroup(toolboxLayout.createParallelGroup(Alignment.CENTER).addComponent(addButton).addComponent(removeButton).addComponent(changeButton));
      toolboxLayout.setVerticalGroup(toolboxLayout.createSequentialGroup().addComponent(addButton).addComponent(removeButton).addComponent(changeButton));
      toolboxLayout.linkSize(SwingConstants.HORIZONTAL, addButton, removeButton, changeButton);
      return toolbox;
    }
    
    void setSubController(final SubController<M, ? extends SubPanel<M>> subController) {
      this.subController = subController;
      this.list.addListSelectionListener(subController);
    }

    private void updateModel(final ListModel<M> model) {
      this.list.setModel(model);
    }

    private class AddAction extends AbstractAction {
      private static final long serialVersionUID = 1L;

      private final String titleKey;
      
      public AddAction(final String stringsKey) {
        super(UI.BUNDLE.getString("prefs.add"));
        this.titleKey = "prefs." + stringsKey + ".new.title";
      }
      
      @Override
      public void actionPerformed(final ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            subController.switchToBlankModel();
            final int result = JOptionPane.showOptionDialog(PreferencesPanel.this, subPanel, UI.BUNDLE.getString(titleKey), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (JOptionPane.OK_OPTION == result) {
              subController.addCurrentModel();
            } else {
              subController.registerModel(list.getSelectedValue());
            }
          }
        });
      }
    }
    
    private class RemoveAction extends AbstractAction {
      private static final long serialVersionUID = 1L;

      private final String confirmKey;
      
      public RemoveAction(final String stringsKey) {
        super(UI.BUNDLE.getString("prefs.remove"));
        this.setEnabled(false);
        this.confirmKey = "prefs." + stringsKey + ".remove.confirm";
      }
      
      @Override
      public void actionPerformed(final ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            final int result = JOptionPane.showConfirmDialog(PreferencesPanel.this, UI.BUNDLE.getString(confirmKey), UI.BUNDLE.getString("prefs.remove"), JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
              subController.remove(list.getSelectedIndex());
            }
          }
        });
      }
    }
    
    private class ChangeAction extends AbstractAction {
      private static final long serialVersionUID = 1L;

      private final String titleKey;

      public ChangeAction(final String stringsKey) {
        super(UI.BUNDLE.getString("prefs.change"));
        this.titleKey = "prefs." + stringsKey + ".change.title";
        this.setEnabled(false);
      }
      
      @Override
      public void actionPerformed(final ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            JOptionPane.showOptionDialog(PreferencesPanel.this, subPanel, UI.BUNDLE.getString(titleKey), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
          }
        });
      }
    }
  }
  
}
