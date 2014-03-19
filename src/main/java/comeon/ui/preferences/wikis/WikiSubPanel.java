package comeon.ui.preferences.wikis;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import comeon.ui.preferences.SubPanel;
import comeon.ui.preferences.input.UrlInputVerifier;

@Singleton
public final class WikiSubPanel extends SubPanel<WikiModel> {
  private static final long serialVersionUID = 1L;
  
  private final JTextField nameField;
  
  private final JTextField urlField;
  
  private final JTextField displayNameField;
  
  private final JTextField loginField;
  
  private final JPasswordField passwordField;
  
  @Inject
  public WikiSubPanel(final WikiSubController subController) {
    this.nameField = new JTextField(COLUMNS);
    this.nameField.setInputVerifier(NOT_BLANK_INPUT_VERIFIER);
    this.urlField = new JTextField(COLUMNS);
    this.urlField.setInputVerifier(new UrlInputVerifier());
    this.displayNameField = new JTextField(COLUMNS);
    this.displayNameField.setInputVerifier(NOT_BLANK_INPUT_VERIFIER);
    this.loginField = new JTextField(COLUMNS);
    this.loginField.setInputVerifier(NOT_BLANK_INPUT_VERIFIER);
    this.passwordField = new JPasswordField(COLUMNS);
    this.passwordField.setInputVerifier(NOT_BLANK_INPUT_VERIFIER);
    this.layoutComponents();
    subController.setView(this);
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
