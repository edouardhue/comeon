package comeon.ui.preferences;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import layout.SpringUtilities;

import comeon.Core;
import comeon.UserNotSetException;
import comeon.model.User;
import comeon.ui.UI;

final class UserSettingsPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private final JTextField displayNameField;

  private final JTextField loginField;

  private final JPasswordField passwordField;
  
  private final User user;

  public UserSettingsPanel() {
    super(new SpringLayout());

    User candidateUser;
    try {
      candidateUser = new User(Core.getInstance().getUsers().getUser());
    } catch (final UserNotSetException e) {
      candidateUser = new User("", "", "");
    }
    this.user = candidateUser;
    
    final JLabel displayNameLabel = new JLabel(UI.BUNDLE.getString("prefs.user.displayName"), SwingConstants.TRAILING);
    this.add(displayNameLabel);
    displayNameField = new JTextField(this.user.getDisplayName(), 20);
    displayNameField.setMaximumSize(displayNameField.getPreferredSize());
    displayNameField.getDocument().addDocumentListener(new DocumentListener() {
      
      @Override
      public void removeUpdate(final DocumentEvent e) {
        this.updateUser(e.getDocument());
      }
      
      @Override
      public void insertUpdate(final DocumentEvent e) {
        this.updateUser(e.getDocument());
      }
      
      @Override
      public void changedUpdate(final DocumentEvent e) {
      }
      
      private void updateUser(final Document doc) {
        try {
          UserSettingsPanel.this.user.setDisplayName(doc.getText(0, doc.getLength()));
        } catch (final BadLocationException e) {
        }
      }
    });
    displayNameLabel.setLabelFor(displayNameField);
    this.add(displayNameField);
    
    final JLabel loginLabel = new JLabel(UI.BUNDLE.getString("prefs.user.login"), SwingConstants.TRAILING);
    this.add(loginLabel);
    loginField = new JTextField(this.user.getLogin(), 10);
    loginField.setMaximumSize(loginField.getPreferredSize());
    loginField.getDocument().addDocumentListener(new DocumentListener() {
      
      @Override
      public void removeUpdate(final DocumentEvent e) {
        this.updateUser(e.getDocument());
      }
      
      @Override
      public void insertUpdate(final DocumentEvent e) {
        this.updateUser(e.getDocument());
      }
      
      @Override
      public void changedUpdate(final DocumentEvent e) {
      }
      
      private void updateUser(final Document doc) {
        try {
          UserSettingsPanel.this.user.setLogin(doc.getText(0, doc.getLength()));
        } catch (final BadLocationException e) {
        }
      }
    });
    loginLabel.setLabelFor(loginField);
    this.add(loginField);

    final JLabel passwordLabel = new JLabel(UI.BUNDLE.getString("prefs.user.password"), SwingConstants.TRAILING);
    this.add(passwordLabel);
    passwordField = new JPasswordField(this.user.getPassword(), 10);
    passwordField.setMaximumSize(passwordField.getPreferredSize());
    passwordField.getDocument().addDocumentListener(new DocumentListener() {
      
      @Override
      public void removeUpdate(final DocumentEvent e) {
        this.updateUser(e.getDocument());
      }
      
      @Override
      public void insertUpdate(final DocumentEvent e) {
        this.updateUser(e.getDocument());
      }
      
      @Override
      public void changedUpdate(final DocumentEvent e) {
      }
      
      private void updateUser(final Document doc) {
        try {
          UserSettingsPanel.this.user.setPassword(doc.getText(0, doc.getLength()));
        } catch (final BadLocationException e) {
        }
      }
    });
    passwordLabel.setLabelFor(passwordField);
    this.add(passwordField);

    SpringUtilities.makeCompactGrid(this, 3, 2, 6, 6, 6, 6);
  }
  
  public User getUser() {
    return user;
  }
}
