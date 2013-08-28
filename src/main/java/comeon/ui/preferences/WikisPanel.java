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

  public WikisPanel(final List<Wiki> wikis) {
    super(new BorderLayout());
    this.tableModel = new WikisTableModel(wikis);
    this.table = new JTable(tableModel);
    this.add(new JScrollPane(table), BorderLayout.CENTER);
    final JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER));
    // TODO add icons
    toolbar.add(new JButton(new AddWikiAction()));
    toolbar.add(new JButton(new RemoveWikiAction()));
    this.add(toolbar, BorderLayout.SOUTH);
  }

  public List<Wiki> getWikis() {
    return tableModel.getWikis();
  }

  // TODO i18n
  private final class AddWikiAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public AddWikiAction() {
      super(UI.BUNDLE.getString("prefs.wikis.add"));
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
      final String name = JOptionPane.showInputDialog(WikisPanel.this, "Name ?");
      final String url = JOptionPane.showInputDialog(WikisPanel.this, "URL ?");
      final Wiki wiki = new Wiki(name, url);
      tableModel.addWiki(wiki);
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

  private static final class WikisTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private final List<Wiki> wikis;

    public WikisTableModel(final List<Wiki> wikis) {
      this.wikis = new ArrayList<>(wikis);
    }

    @Override
    public int getRowCount() {
      return wikis.size();
    }

    @Override
    public int getColumnCount() {
      return 2;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
      final Wiki wiki = wikis.get(rowIndex);
      final String value;
      switch (columnIndex) {
      case 0:
        value = wiki.getName();
        break;
      case 1:
        value = wiki.getUrl();
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
  }
}
