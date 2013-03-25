package comeon.ui.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comeon.Core;
import comeon.FailedLoginException;
import comeon.FailedLogoutException;
import comeon.FailedUploadException;
import comeon.NotLoggedInException;
import comeon.UserNotSetException;
import comeon.ui.UI;

public final class UploadPicturesAction extends BaseAction {
  private static final Logger LOGGER = LoggerFactory.getLogger(UploadPicturesAction.class);

  private static final long serialVersionUID = 1L;

  public UploadPicturesAction(final UI ui) {
    super("upload", ui);
  }
  
  @Override
  public void actionPerformed(final ActionEvent e) {
    try {
      Core.getInstance().uploadPictures();
    } catch (final NotLoggedInException | FailedLoginException | FailedUploadException | UserNotSetException | FailedLogoutException | IOException ex) {
      // TODO i18n
      LOGGER.error("Upload error", ex);
      SwingUtilities.invokeLater(new Runnable() {
        
        @Override
        public void run() {
          JOptionPane.showMessageDialog(ui, ex.getMessage(), "Upload error", JOptionPane.ERROR_MESSAGE);
        }
      });
    }
  }

}
