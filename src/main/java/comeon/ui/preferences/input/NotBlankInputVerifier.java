package comeon.ui.preferences.input;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.StringUtils;

public final class NotBlankInputVerifier extends InputVerifier {

  @Override
  public boolean verify(final JComponent input) {
    final boolean isValid;
    if (input instanceof JTextComponent) {
      final JTextComponent textComponent = (JTextComponent) input;
      final String text = textComponent.getText();
      isValid = StringUtils.isNotBlank(text);
    } else {
      isValid = true;
    }
    return isValid;
  }

}
