package comeon.ui.actions;

import in.yuvi.http.fluent.ProgressListener;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import comeon.core.Core;
import comeon.core.UploadMonitor;
import comeon.ui.UI;

@Singleton
public final class UploadPicturesAction extends BaseAction {
  private static final long serialVersionUID = 1L;

  private final Core core;

  @Inject
  public UploadPicturesAction(final Core core) {
    super("upload");
    this.core = core;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    core.uploadPictures(new Monitor());
  }

  private final class Monitor extends JOptionPane implements UploadMonitor {
    private static final long serialVersionUID = 1L;

    private final JDialog dialog;

    private final JProgressBar batchBar;

    private final JProgressBar pictureBar;

    public Monitor() {
      super(null, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[0]);
      this.batchBar = new JProgressBar(SwingConstants.HORIZONTAL);
      this.pictureBar = new JProgressBar(SwingConstants.HORIZONTAL);
      this.pictureBar.setStringPainted(true);
      this.setMessage(new Object[] { batchBar, pictureBar });
      this.dialog = this.createDialog(JOptionPane.getRootFrame(), UI.BUNDLE.getString("upload.title"));
    }

    @Override
    public void setBatchSize(final int size) {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          Monitor.this.batchBar.setMaximum(size);
        }
      });
    }

    @Override
    public void uploadStarting() {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          Monitor.this.batchBar.setValue(0);
          Monitor.this.dialog.setVisible(true);
        }
      });
    }

    @Override
    public ProgressListener itemStarting(final int index, final long length, final String name) {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          Monitor.this.pictureBar.setMaximum((int) length);
          Monitor.this.pictureBar.setValue(0);
          Monitor.this.batchBar.setString(name);
          Monitor.this.batchBar.setStringPainted(true);
          Monitor.this.dialog.pack();
        }
      });
      return new ProgressListener() {
        @Override
        public void onProgress(final long transferred, final long total) {
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              Monitor.this.pictureBar.setValue((int) transferred);
            }
          });
        }
      };
    }

    @Override
    public void itemDone(final int index) {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          Monitor.this.batchBar.setValue(index + 1);
        }
      });
    }

    @Override
    public void uploadDone() {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          Monitor.this.dialog.setVisible(false);
        }
      });
    }

  }
}
