package comeon.ui.actions;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;
import comeon.ui.UI;

public abstract class BaseAction extends AbstractAction {
  private static final Logger LOGGER = LoggerFactory.getLogger(BaseAction.class);

  private static final long serialVersionUID = 1L;

  protected BaseAction(final String key) {
    this(key, null);
  }

  protected BaseAction(final String key, final KeyStroke accelerator) {
    super(UI.BUNDLE.getString("action." + key + ".title"));
    this.setKeys(key, accelerator);
    this.setIcon(key);
  }
  
  private void setIcon(final String key) {
    setIcon("comeon/ui/" + key + "_small.png", Action.SMALL_ICON);
    setIcon("comeon/ui/" + key + "_large.png", Action.LARGE_ICON_KEY);
  }
  
  private void setIcon(final String resourcePath, final String valueKey) {
    try {
      final URL iconUrl = Resources.getResource(resourcePath);
      final ImageIcon icon = new ImageIcon(iconUrl);
      this.putValue(valueKey, icon);
    } catch (final IllegalArgumentException e) {
      LOGGER.debug("No icon found at path {}", resourcePath, e);
    }
  }
  
  private void setKeys(final String key, final KeyStroke accelerator) {
    setMnemoKey(key);
    setAccelerator(key, accelerator);
  }

  private void setAccelerator(final String key, final KeyStroke accelerator) {
    if (accelerator == null) {
      final String acceleratorKey = "action." + key + ".accel";
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

  private void setMnemoKey(final String key) {
    final String mnemoKey = "action." + key + ".mnemo";
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
