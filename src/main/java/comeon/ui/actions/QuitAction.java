package comeon.ui.actions;

import java.awt.event.ActionEvent;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public final class QuitAction extends BaseAction {

  private static final long serialVersionUID = 1L;

  @Inject
  public QuitAction() {
    super("quit");
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    System.exit(0);
  }

}
