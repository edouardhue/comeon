package comeon.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import com.google.common.eventbus.Subscribe;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.Core;
import comeon.ui.UI;
import comeon.wikis.Wikis;

@Singleton
public final class UploadMediaAction extends BaseAction {
  private static final long serialVersionUID = 1L;

  static final ImageIcon ICON = new ImageIcon(Resources.getResource("comeon/ui/upload_huge.png"));

  private final Core core;

  private final Wikis wikis;
  
  @Inject
  public UploadMediaAction(final Core core, final Wikis wikis) {
    super("upload");
    this.core = core;
    this.wikis = wikis;
    this.setEnabled(false);
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    final int mediaToUpload = core.countMediaToBeUploaded();
    if (mediaToUpload == 0) {
      JOptionPane.showMessageDialog(
          SwingUtilities.getWindowAncestor((Component) e.getSource()),
          UI.BUNDLE.getString("action.upload.none"));
    } else {
      final int choice = JOptionPane.showConfirmDialog(
          SwingUtilities.getWindowAncestor((Component) e.getSource()),
          MessageFormat.format(
              UI.BUNDLE.getString("action.upload.confirm"),
              mediaToUpload, wikis.getActiveWiki().getName()),
              UIManager.getString("OptionPane.titleText"),
              JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              ICON);
      if (JOptionPane.OK_OPTION == choice) {
        new SwingWorker<Void, Void>() {
          @Override
          protected Void doInBackground() throws Exception {
            core.uploadMedia();
            return null;
          }
        }.execute();
      }
    }
  }

  @Subscribe
  public void handleMediaAddedEvent(final MediaAddedEvent event) {
    this.enableIfMediaAreAvailable();
  }

  @Subscribe
  public void handleMediaRemovedEvent(final MediaRemovedEvent event) {
    this.enableIfMediaAreAvailable();
  }
  
  private void enableIfMediaAreAvailable() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        UploadMediaAction.this.setEnabled(core.countMediaToBeUploaded() > 0);
      }
    });
  }
}
