package comeon.ui.preferences.wikis;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import comeon.ui.preferences.SubPanel;

public final class WikiSubPanel extends SubPanel<WikiModel> {
  private static final long serialVersionUID = 1L;
  
  private final JTextField nameField;
  
  private final JTextField urlField;
  
  private final JTextField displayNameField;
  
  private final JTextField loginField;
  
  private final JPasswordField passwordField;
  
  public WikiSubPanel() {
    this.nameField = new JTextField();
    this.urlField = new JTextField();
    this.displayNameField = new JTextField();
    this.loginField = new JTextField();
    this.passwordField = new JPasswordField();
    this.layoutComponents();
  }

  JTextField getNameField() {
    return nameField;
  }

  JTextField getUrlField() {
    return urlField;
  }

  JTextField getDisplayNameField() {
    return displayNameField;
  }

  JTextField getLoginField() {
    return loginField;
  }

  JPasswordField getPasswordField() {
    return passwordField;
  }

  @Override
  protected void doLayoutComponents(GroupLayout layout) {
    final JLabel[] labels = new JLabel[] {
        new AssociatedLabel("prefs.wikis.name", nameField),
        new AssociatedLabel("prefs.wikis.url", urlField),
        new AssociatedLabel("prefs.wikis.displayName", displayNameField),
        new AssociatedLabel("prefs.wikis.login", loginField),
        new AssociatedLabel("prefs.wikis.password", passwordField)
    };
    layout.setVerticalGroup(
        layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup().addComponent(labels[0]).addComponent(nameField))
        .addGroup(layout.createParallelGroup().addComponent(labels[1]).addComponent(urlField))
        .addGroup(layout.createParallelGroup().addComponent(labels[2]).addComponent(displayNameField))
        .addGroup(layout.createParallelGroup().addComponent(labels[3]).addComponent(loginField))
        .addGroup(layout.createParallelGroup().addComponent(labels[4]).addComponent(passwordField))
    );
    layout.setHorizontalGroup(
        layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup().addComponent(labels[0]).addComponent(labels[1]).addComponent(labels[2]).addComponent(labels[3]).addComponent(labels[4]))
        .addGroup(layout.createParallelGroup().addComponent(nameField).addComponent(urlField).addComponent(displayNameField).addComponent(loginField).addComponent(passwordField))
    );
  }
}
