package comeon.ui.preferences;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import layout.SpringUtilities;

import comeon.model.User;
import comeon.model.Wiki;
import comeon.ui.UI;

final class WikiPanel extends JOptionPane {

  private static final long serialVersionUID = 1L;

  private final JDialog dialog;
  
  private final JPanel fieldsPanel;
  
  private final JTextField nameField;
  
  private final JTextField urlField;
  
  private final JTextField displayNameField;
  
  private final JTextField loginField;
  
  private final JPasswordField passwordField;
  
  public WikiPanel(final Wiki wiki) {
    super(null, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
    this.fieldsPanel = new JPanel(new SpringLayout());
    this.nameField = new JTextField(wiki == null ? "" : wiki.getName(), 20);
    this.urlField = new JTextField(wiki == null ? "" : wiki.getUrl(), 45);
    this.displayNameField = new JTextField(wiki == null ? "" : wiki.getUser().getDisplayName(), 20);
    this.loginField = new JTextField(wiki == null ? "" : wiki.getUser().getLogin(), 10);
    this.passwordField = new JPasswordField(wiki == null ? "" : wiki.getUser().getPassword(), 10);
    this.build();
    this.setMessage(this.fieldsPanel);
    this.dialog = this.createDialog(JOptionPane.getRootFrame(), UI.BUNDLE.getString("action.preferences.title"));
  }
  
  private void build() {
    final JLabel nameLabel = new JLabel(UI.BUNDLE.getString("prefs.wikis.name"), SwingConstants.TRAILING);
    nameLabel.setLabelFor(nameField);
    fieldsPanel.add(nameLabel);
    fieldsPanel.add(this.nameField);
    final JLabel urlLabel = new JLabel(UI.BUNDLE.getString("prefs.wikis.url"), SwingConstants.TRAILING);
    urlLabel.setLabelFor(urlField);
    fieldsPanel.add(urlLabel);
    fieldsPanel.add(this.urlField);
    final JLabel displayNameLabel = new JLabel(UI.BUNDLE.getString("prefs.wikis.displayName"), SwingConstants.TRAILING);
    displayNameLabel.setLabelFor(displayNameField);
    fieldsPanel.add(displayNameLabel);
    fieldsPanel.add(this.displayNameField);
    final JLabel loginLabel = new JLabel(UI.BUNDLE.getString("prefs.wikis.login"), SwingConstants.TRAILING);
    loginLabel.setLabelFor(loginField);
    fieldsPanel.add(loginLabel);
    fieldsPanel.add(this.loginField);
    final JLabel passwordLabel = new JLabel(UI.BUNDLE.getString("prefs.wikis.password"), SwingConstants.TRAILING);
    passwordLabel.setLabelFor(passwordField);
    fieldsPanel.add(passwordLabel);
    fieldsPanel.add(this.passwordField);

    SpringUtilities.makeCompactGrid(fieldsPanel, 5, 2, 6, 6, 6, 6);
  }

  public int showDialog() {
    this.dialog.setVisible(true);
    return ((Integer) this.getValue()).intValue();
  }
  
  Wiki getWiki() {
    return new Wiki(this.nameField.getText(), this.urlField.getText(), new User(this.loginField.getText(), new String(this.passwordField.getPassword()), this.displayNameField.getText()));
  }
}
