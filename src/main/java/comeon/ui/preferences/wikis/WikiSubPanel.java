package comeon.ui.preferences.wikis;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.netbeans.validation.api.builtin.stringvalidation.StringValidators;
import org.netbeans.validation.api.ui.ValidationGroup;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import comeon.ui.UI;
import comeon.ui.preferences.SubPanel;

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
    this.nameField.setName(UI.BUNDLE.getString("prefs.wikis.name"));
    this.nameField.requestFocusInWindow();
    this.urlField = new JTextField(COLUMNS);
    this.urlField.setName(UI.BUNDLE.getString("prefs.wikis.url"));
    this.displayNameField = new JTextField(COLUMNS);
    this.displayNameField.setName(UI.BUNDLE.getString("prefs.wikis.displayName"));
    this.loginField = new JTextField(COLUMNS);
    this.loginField.setName(UI.BUNDLE.getString("prefs.wikis.login"));
    this.passwordField = new JPasswordField(COLUMNS);
    this.passwordField.setName(UI.BUNDLE.getString("prefs.wikis.password"));
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
  protected void doLayoutComponents(final GroupLayout layout) {
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
  
  @Override
  protected void doAttach(final ValidationGroup validationGroup) {
    validationGroup.add(nameField, StringValidators.REQUIRE_NON_EMPTY_STRING);
    validationGroup.add(urlField, StringValidators.URL_MUST_BE_VALID);
    validationGroup.add(displayNameField, StringValidators.REQUIRE_NON_EMPTY_STRING);
    validationGroup.add(loginField, StringValidators.REQUIRE_NON_EMPTY_STRING);
    validationGroup.add(passwordField, StringValidators.REQUIRE_NON_EMPTY_STRING);    
  }
}
