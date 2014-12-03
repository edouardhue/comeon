package comeon.ui.actions;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import comeon.core.ProgressListenerAdapter;
import comeon.core.events.PictureTransferDoneEvent;
import comeon.core.events.PictureTransferFailedEvent;
import comeon.core.events.PictureTransferStartingEvent;
import comeon.core.events.UploadDoneEvent;
import comeon.core.events.UploadStartingEvent;
import comeon.model.Picture;
import comeon.ui.UI;

@Singleton
public final class TransferMonitor extends JOptionPane {
  private static final long serialVersionUID = 1L;

  private final JDialog dialog;

  private final JProgressBar batchBar;

  private final Box pictureBarsBox;

  private final JScrollPane pictureBarsPane;

  private final CloseAction closeAction;
  
  private final Map<File, ProgressPanel> panels;
  
  private final AtomicInteger transferCounter;
  
  @Inject
  public TransferMonitor(final AbortAction abortAction, final UI ui) {
    super(null, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, UploadPicturesAction.ICON, null);
    this.getInputMap(JOptionPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed ESCAPE"), "none");
    this.batchBar = new JProgressBar(SwingConstants.HORIZONTAL);
    this.batchBar.setStringPainted(true);
    this.pictureBarsBox = Box.createVerticalBox();
    this.pictureBarsPane = new JScrollPane(pictureBarsBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    this.setMessage(new Object[] { batchBar, pictureBarsPane });
    final Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    this.dialog = this.createDialog(JOptionPane.getRootFrame(), UI.BUNDLE.getString("upload.title"));
    this.dialog.setResizable(true);
    this.dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.dialog.setIconImages(UI.ICON_IMAGES);
    this.dialog.setSize(new Dimension(screenSize.width / 2, screenSize.height / 2));
    this.dialog.setLocationRelativeTo(ui);
    this.closeAction = new CloseAction();
    this.setOptions(new Object[] { new JButton(closeAction), new JButton(abortAction) });
    this.panels = new HashMap<>();
    this.transferCounter = new AtomicInteger(0);
    this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "abort");
    this.getActionMap().put("abort", abortAction);
  }

  @Subscribe
  public void uploadStarting(final UploadStartingEvent event) {
    for (final Picture picture : event.getPictures()) {
      final ProgressPanel panel = new ProgressPanel(picture.getFile().length(), picture.getFileName());
      panels.put(picture.getFile(), panel);
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          pictureBarsBox.add(panel);
        }
      });
    }
    transferCounter.set(0);
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        batchBar.setMaximum(event.getPictures().size());
        batchBar.setValue(transferCounter.get());
        dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        closeAction.setEnabled(false);
        dialog.setVisible(true);
      }
    });
  }

  @Subscribe
  public void transferStarting(final PictureTransferStartingEvent event) {
    final ProgressPanel panel = panels.get(event.getPicture().getFile());
    event.getProgressListener().addPropertyChangeListener(ProgressListenerAdapter.TRANSFERRED, new PropertyChangeListener() {
      @Override
      public void propertyChange(final PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            final Long transferred = (Long) evt.getNewValue();
            panel.getPictureBar().setValue(transferred.intValue());
          }
        });
      }
    });
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        pictureBarsPane.getViewport().scrollRectToVisible(panel.getBounds());        
      }
    });
  }
  
  @Subscribe
  public void transferDone(final PictureTransferDoneEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        batchBar.setValue(transferCounter.incrementAndGet());
      }
    });
  }

  @Subscribe
  public void transferFailed(final PictureTransferFailedEvent event) {
    final JProgressBar pictureProgressBar = panels.get(event.getPicture().getFile()).getPictureBar();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        pictureProgressBar.setValue(pictureProgressBar.getMaximum());
        pictureProgressBar.setString(UI.BUNDLE.getString("error.generic.title"));
        pictureProgressBar.setToolTipText(MessageFormat.format(UI.BUNDLE.getString("error.upload.failed"), event.getCause().getLocalizedMessage()));
      }
    });
  }
  
  @Subscribe
  public void uploadDone(final UploadDoneEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        TransferMonitor.this.dialog.setCursor(Cursor.getDefaultCursor());
        TransferMonitor.this.closeAction.setEnabled(true);
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
      new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
          panels.clear();
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              dialog.setVisible(false);
              pictureBarsBox.removeAll();
            }
          });
          return null;
        }
      }.execute();
    }
  }
  
  static class ProgressPanel extends Box {
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