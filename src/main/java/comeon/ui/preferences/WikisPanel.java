package comeon.ui.preferences;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import comeon.model.Wiki;
import comeon.ui.UI;

public final class WikisPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private final WikisTableModel tableModel;

  private final JTable table;

  public WikisPanel(final List<Wiki> wikis, final int defaultWikiIndex) {
    super(new BorderLayout());
    this.tableModel = new WikisTableModel(wikis, defaultWikiIndex);
    this.table = new JTable(tableModel);
    this.add(new JScrollPane(table), BorderLayout.CENTER);
    final JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER));
    // TODO add icons
    toolbar.add(new JButton(new AddWikiAction()));
    toolbar.add(new JButton(new EditWikiAction()));
    toolbar.add(new JButton(new RemoveWikiAction()));
    toolbar.add(new JButton(new MakeDefaultAction()));
    this.add(toolbar, BorderLayout.SOUTH);
  }

  public List<Wiki> getWikis() {
    return tableModel.getWikis();
  }
  
  public Wiki getActiveWiki() {
    return tableModel.getWikis().get(tableModel.defaultWikiIndex);
  }

  private final class AddWikiAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public AddWikiAction() {
      super(UI.BUNDLE.getString("prefs.wikis.add"));
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
      final WikiPanel panel = new WikiPanel(null);
      final int value = panel.showDialog();
      if (value == JOptionPane.OK_OPTION) {
        final Wiki wiki = panel.getWiki();
        tableModel.addWiki(wiki);
      }
    }
  }
  
  private final class EditWikiAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    
    public EditWikiAction() {
      super(UI.BUNDLE.getString("prefs.wikis.edit"));
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
      final int selectedRow = table.getSelectedRow();
      if (selectedRow != -1) {
        final Wiki selectedWiki = tableModel.getWikis().get(selectedRow);
        final WikiPanel panel = new WikiPanel(selectedWiki);
        final int value = panel.showDialog();
        if (value == JOptionPane.OK_OPTION) {
          final Wiki editedWiki = panel.getWiki();
          tableModel.replaceWiki(selectedRow, editedWiki);
        }
      }
    }
  }

  private final class RemoveWikiAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public RemoveWikiAction() {
      super(UI.BUNDLE.getString("prefs.wikis.remove"));
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
      final int selectedRow = table.getSelectedRow();
      if (selectedRow != -1) {
        tableModel.removeRow(selectedRow);
      }
    }
  }
  
  private final class MakeDefaultAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
  
    public MakeDefaultAction() {
      super(UI.BUNDLE.getString("prefs.wikis.makeDefault"));
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
      tableModel.setDefault(table.getSelectedRow());
      
    }
  }

  private static final class WikisTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private final List<Wiki> wikis;

    private int defaultWikiIndex;
    
    public WikisTableModel(final List<Wiki> wikis, final int defaultWikiIndex) {
      this.wikis = new ArrayList<>(wikis);
      this.defaultWikiIndex = defaultWikiIndex;
    }

    @Override
    public int getRowCount() {
      return wikis.size();
    }

    @Override
    public int getColumnCount() {
      return 5;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
      final Wiki wiki = wikis.get(rowIndex);
      final Object value;
      switch (columnIndex) {
      case 0:
        value = wiki.getName();
        break;
      case 1:
        value = wiki.getUrl();
        break;
      case 2:
        value = wiki.getUser().getDisplayName();
        break;
      case 3:
        value = wiki.getUser().getLogin();
        break;
      case 4:
          value = defaultWikiIndex == rowIndex;
          break;
      default:
        throw new IllegalArgumentException();
      }
      return value;
    }

    private List<Wiki> getWikis() {
      return new ArrayList<>(wikis);
    }

    private void addWiki(final Wiki wiki) {
      wikis.add(wiki);
      final int lastRow = wikis.size() - 1;
      this.fireTableRowsInserted(lastRow, lastRow);
    }

    private void removeRow(final int row) {
      wikis.remove(row);
      this.fireTableRowsDeleted(row, row);
    }
    
    private void replaceWiki(final int row, final Wiki wiki) {
      wikis.set(row, wiki);
      this.fireTableRowsUpdated(row, row);
    }
    
    private void setDefault(final int index) {
      final int previousIndex = this.defaultWikiIndex;
      this.defaultWikiIndex = index;
      this.fireTableRowsUpdated(previousIndex, previousIndex);
      this.fireTableRowsUpdated(index, index);
    }
  }
}
