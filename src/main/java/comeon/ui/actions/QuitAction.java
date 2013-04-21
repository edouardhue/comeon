package comeon.ui.actions;

import java.awt.event.ActionEvent;

import comeon.ui.UI;

public final class QuitAction extends BaseAction {

  private static final long serialVersionUID = 1L;

  public QuitAction(final UI ui) {
    super("quit", ui);
  }
  
  @Override
  public void actionPerformed(final ActionEvent e) {
    System.exit(0);
  }

}
