package comeon.ui.actions;

import in.yuvi.http.fluent.ProgressListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.google.common.eventbus.Subscribe;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.Core;
import comeon.core.UploadMonitor;
import comeon.ui.UI;
import comeon.wikis.Wikis;

@Singleton
public final class UploadPicturesAction extends BaseAction {
  private static final long serialVersionUID = 1L;

  private static final ImageIcon ICON = new ImageIcon(Resources.getResource("comeon/ui/upload_huge.png"));

  private final Core core;

  private final Wikis wikis;
  
  @Inject
  public UploadPicturesAction(final Core core, final Wikis wikis) {
    super("upload");
    this.core = core;
    this.wikis = wikis;
    this.setEnabled(false);
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    final int picturesToUpload = core.countPicturesToBeUploaded();
    if (picturesToUpload == 0) {
      JOptionPane.showMessageDialog(
          SwingUtilities.getWindowAncestor((Component) e.getSource()),
          UI.BUNDLE.getString("action.upload.none"));
    } else {
      final int choice = JOptionPane.showConfirmDialog(
          SwingUtilities.getWindowAncestor((Component) e.getSource()),
          MessageFormat.format(
              UI.BUNDLE.getString("action.upload.confirm"),
              picturesToUpload, wikis.getActiveWiki().getName()),
              UIManager.getString("OptionPane.titleText"),
              JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              ICON);
      if (JOptionPane.OK_OPTION == choice) {
        new SwingWorker<Void, Void>() {
          @Override
          protected Void doInBackground() throws Exception {
            core.uploadPictures(new Monitor());
            return null;
          }
        }.execute();
      }
    }
  }

  @Subscribe
  public void handlePicturesAddedEvent(final PicturesAddedEvent event) {
    this.enableIfPicturesAreAvailable();
  }

  @Subscribe
  public void handlePictureRemovedEvent(final PictureRemovedEvent event) {
    this.enableIfPicturesAreAvailable();
  }
  
  private void enableIfPicturesAreAvailable() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        UploadPicturesAction.this.setEnabled(core.countPicturesToBeUploaded() > 0);
      }
    });
  }
  
  private final class Monitor extends JOptionPane implements UploadMonitor {
    private static final long serialVersionUID = 1L;

    private final JDialog dialog;

    private final JProgressBar batchBar;

    private final Box pictureBarsBox;

    private final JScrollPane pictureBarsPane;

    private final CloseAction closeAction;
    
    private ProgressPanel[] panels;

    public Monitor() {
      super(null, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, ICON, null);
      this.batchBar = new JProgressBar(SwingConstants.HORIZONTAL);
      this.batchBar.setStringPainted(true);
      this.pictureBarsBox = Box.createVerticalBox();
      this.pictureBarsPane = new JScrollPane(pictureBarsBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
          JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      this.setMessage(new Object[] { batchBar, pictureBarsPane });
      this.dialog = this.createDialog(JOptionPane.getRootFrame(), UI.BUNDLE.getString("upload.title"));
      this.dialog.setResizable(true);
      this.dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      this.dialog.setIconImages(UI.ICON_IMAGES);
      this.closeAction = new CloseAction();
      this.setOptions(new Object[] { new JButton(closeAction) });
    }

    @Override
    public void setBatchSize(final int size) {
      this.panels = new ProgressPanel[size];
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
          Monitor.this.dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
          Monitor.this.closeAction.setEnabled(false);
          Monitor.this.batchBar.setValue(0);
          Monitor.this.dialog.setVisible(true);
        }
      });
    }

    @Override
    public ProgressListener itemStarting(final int index, final long length, final String name) {
      final ProgressPanel panel = new ProgressPanel(length, name);
      panels[index] = panel;
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          pictureBarsBox.add(panel);
          final int preferredWidth = (panel.getPreferredSize().width > pictureBarsPane.getViewport().getPreferredSize().width ? panel
              .getPreferredSize().width : pictureBarsPane.getViewport().getPreferredSize().width);
          pictureBarsPane.getViewport().setPreferredSize(
              new Dimension(preferredWidth, panel.getPreferredSize().height * 3));
          Monitor.this.dialog.pack();
          pictureBarsPane.getViewport().scrollRectToVisible(panel.getBounds());
        }
      });
      return new ProgressListener() {
        @Override
        public void onProgress(final long transferred, final long total) {
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              panel.getPictureBar().setValue((int) transferred);
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
          Monitor.this.batchBar.setValue(batchBar.getValue() + 1);
        }
      });
    }
    
    @Override
    public void itemFailed(final int index, final Exception cause) {
      itemDone(index);
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          final JProgressBar pictureProgressBar = panels[index].getPictureBar();
          pictureProgressBar.setValue(pictureProgressBar.getMaximum());
          pictureProgressBar.setString(UI.BUNDLE.getString("error.generic.title"));
          pictureProgressBar.setToolTipText(MessageFormat.format(UI.BUNDLE.getString("error.upload.failed"), cause.getLocalizedMessage()));
        }
      });
    }

    @Override
    public void uploadDone() {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          Monitor.this.dialog.setCursor(Cursor.getDefaultCursor());
          Monitor.this.closeAction.setEnabled(true);
        }
      });
    }

    private final class CloseAction extends AbstractAction {
      private static final long serialVersionUID = 1L;

      public CloseAction() {
        super(UIManager.getString("OptionPane.okButtonText", Locale.getDefault()));
      }

      @Override
      public void actionPerformed(final ActionEvent e) {
        dialog.setVisible(false);
      }
    }
  }

  private static class ProgressPanel extends Box {
    private static final long serialVersionUID = 1L;

    private final JProgressBar pictureBar;

    public ProgressPanel(final long length, final String name) {
      super(BoxLayout.Y_AXIS);
      final JLabel label = new JLabel(name);
      label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
      label.setToolTipText(name);
      this.setBackground(Color.WHITE);
      this.setOpaque(true);
      this.pictureBar = new JProgressBar(SwingConstants.HORIZONTAL);
      this.pictureBar.setStringPainted(true);
      this.pictureBar.setMaximum((int) length);
      this.pictureBar.setValue(0);
      this.pictureBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
      this.add(label);
      this.add(this.pictureBar);
      this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    }

    public JProgressBar getPictureBar() {
      return pictureBar;
    }
  }
}
