package comeon.ui.actions;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import comeon.ui.UI;

public abstract class BaseAction extends AbstractAction {

  private static final long serialVersionUID = 1L;

  protected BaseAction(final String bundleKey) {
    super(UI.BUNDLE.getString("action." + bundleKey + ".title"));
    this.setKeys(bundleKey, null);
  }

  protected BaseAction(final String bundleKey, final KeyStroke accelerator) {
    super(UI.BUNDLE.getString("action." + bundleKey + ".title"));
    this.setKeys(bundleKey, accelerator);
  }

  private void setKeys(final String bundleKey, final KeyStroke accelerator) {
    setMnemoKey(bundleKey);
    setAccelerator(bundleKey, accelerator);
  }

  private void setAccelerator(final String bundleKey, final KeyStroke accelerator) {
    if (accelerator == null) {
      final String acceleratorKey = "action." + bundleKey + ".accel";
      if (UI.BUNDLE.containsKey(acceleratorKey)) {
        final String bundleAccelerator = UI.BUNDLE.getString(acceleratorKey);
        if (!bundleAccelerator.isEmpty()) {
          final int accelKeyEvent = KeyEvent.getExtendedKeyCodeForChar(bundleAccelerator.charAt(0));
          this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelKeyEvent, InputEvent.CTRL_DOWN_MASK));
        }
      }
    } else {
      this.putValue(ACCELERATOR_KEY, accelerator);
    }
  }

  private void setMnemoKey(final String bundleKey) {
    final String mnemoKey = "action." + bundleKey + ".mnemo";
    if (UI.BUNDLE.containsKey(mnemoKey)) {
      final String bundleMnemo = UI.BUNDLE.getString(mnemoKey);
      if (!bundleMnemo.isEmpty()) {
        final char mnemo = bundleMnemo.charAt(0);
        final int mnemoKeyEvent = KeyEvent.getExtendedKeyCodeForChar(mnemo);
        this.putValue(MNEMONIC_KEY, mnemoKeyEvent);
      }
    }
  }
}
