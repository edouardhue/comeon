package comeon.ui.preferences;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.prefs.BackingStoreException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import comeon.Core;

public final class PreferencesPanel extends JDialog {

  private static final long serialVersionUID = 1L;
  
  private final UserSettingsPanel userSettingsPanel;

  public PreferencesPanel(final Window parent) {
    // TODO i18n
    super(parent, "Preferences", ModalityType.APPLICATION_MODAL);
    this.add(new Buttons(), BorderLayout.SOUTH);
    userSettingsPanel = new UserSettingsPanel();
    this.add(userSettingsPanel, BorderLayout.CENTER);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setMinimumSize(new Dimension(300, 200));
    this.setVisible(true);
  }
  
  private final class Buttons extends JPanel {
    private static final long serialVersionUID = 1L;

    private Buttons() {
      super(new FlowLayout(FlowLayout.CENTER));
      this.add(new JButton(new OkAction()));
      this.add(new JButton(new CancelAction()));
    }
  }
  
  private final class OkAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public OkAction() {
      super("OK");
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
      Core.getInstance().getPool().submit(new Runnable() {
        @Override
        public void run() {
          try {
            Core.getInstance().getUsers().setUser(userSettingsPanel.getUser());
          } catch (final BackingStoreException e) {
            SwingUtilities.invokeLater(new Runnable() {
              @Override
              public void run() {
                // TODO i18n
                JOptionPane.showMessageDialog(PreferencesPanel.this.getParent(), e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              }
            });
          }
        }
      });
      PreferencesPanel.this.dispose();
    }
  }
  
  private final class CancelAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public CancelAction() {
      super("Cancel");
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
      PreferencesPanel.this.dispose();
    }
  }
}
