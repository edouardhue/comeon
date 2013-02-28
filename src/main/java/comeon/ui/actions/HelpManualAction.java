package comeon.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

public final class HelpManualAction extends BaseAction {

  private static final long serialVersionUID = 1L;
  
  public HelpManualAction() {
    super("manual", KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    // TODO Auto-generated method stub

  }

}
