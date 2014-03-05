package comeon.ui.preferences.input;

import java.net.URI;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

public final class UrlInputVerifier extends InputVerifier {

  @Override
  public boolean verify(final JComponent input) {
    final boolean isValid;
    if (input instanceof JTextComponent) {
      final JTextComponent textComponent = (JTextComponent) input;
      final String text = textComponent.getText();
      boolean creationSuccessful;
      try {
        final URI uri = URI.create(text);
        creationSuccessful = uri.isAbsolute() && uri.getScheme().startsWith("http");
      } catch (final IllegalArgumentException e) {
        creationSuccessful = false;
      }
      isValid = creationSuccessful;
    } else {
      isValid = true;
    }
    return isValid;
  }

}
