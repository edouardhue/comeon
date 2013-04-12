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
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import comeon.Core;

public final class PreferencesDialog extends JDialog {

  private static final long serialVersionUID = 1L;
  
  private final UserSettingsPanel userSettingsPanel;
  
  private final TemplatesPanel templatesPanel;

  public PreferencesDialog(final Window parent) {
    // TODO i18n
    super(parent, "Preferences", ModalityType.APPLICATION_MODAL);
    this.setLocationRelativeTo(parent);
    this.add(new Buttons(), BorderLayout.SOUTH);
    final JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT);
    this.userSettingsPanel = new UserSettingsPanel();
    tabs.add("User", userSettingsPanel);
    this.templatesPanel = new TemplatesPanel(Core.getInstance().getTemplates().getTemplates());
    tabs.add("Templates", templatesPanel);
    this.add(tabs, BorderLayout.CENTER);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setMinimumSize(new Dimension(480, 320));
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
            Core.getInstance().getTemplates().setTemplates(templatesPanel.getTemplates());
            Core.getInstance().getTemplates().storePreferences();
          } catch (final BackingStoreException e) {
            SwingUtilities.invokeLater(new Runnable() {
              @Override
              public void run() {
                // TODO i18n
                JOptionPane.showMessageDialog(PreferencesDialog.this.getParent(), e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              }
            });
          }
        }
      });
      PreferencesDialog.this.dispose();
    }
  }
  
  private final class CancelAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public CancelAction() {
      super("Cancel");
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
      PreferencesDialog.this.dispose();
    }
  }
}
