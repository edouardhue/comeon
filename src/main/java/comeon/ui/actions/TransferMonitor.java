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
import comeon.core.events.MediaTransferDoneEvent;
import comeon.core.events.MediaTransferFailedEvent;
import comeon.core.events.MediaTransferStartingEvent;
import comeon.core.events.UploadDoneEvent;
import comeon.core.events.UploadStartingEvent;
import comeon.model.Media;
import comeon.ui.UI;

@Singleton
public final class TransferMonitor extends JOptionPane {
  private static final long serialVersionUID = 1L;

  private final JDialog dialog;

  private final JProgressBar batchBar;

  private final Box mediaBarsBox;

  private final JScrollPane mediaBarsPane;

  private final CloseAction closeAction;
  
  private final Map<File, ProgressPanel> panels;
  
  private final AtomicInteger transferCounter;
  
  @Inject
  public TransferMonitor(final AbortAction abortAction, final UI ui) {
    super(null, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, UploadMediaAction.ICON, null);
    this.getInputMap(JOptionPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed ESCAPE"), "none");
    this.batchBar = new JProgressBar(SwingConstants.HORIZONTAL);
    this.batchBar.setStringPainted(true);
    this.mediaBarsBox = Box.createVerticalBox();
    this.mediaBarsPane = new JScrollPane(mediaBarsBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    this.setMessage(new Object[] { batchBar, mediaBarsPane });
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
    for (final Media media : event.getMedia()) {
      final ProgressPanel panel = new ProgressPanel(media.getFile().length(), media.getFileName());
      panels.put(media.getFile(), panel);
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          mediaBarsBox.add(panel);
        }
      });
    }
    transferCounter.set(0);
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        batchBar.setMaximum(event.getMedia().size());
        batchBar.setValue(transferCounter.get());
        dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        closeAction.setEnabled(false);
        dialog.setVisible(true);
      }
    });
  }

  @Subscribe
  public void transferStarting(final MediaTransferStartingEvent event) {
    final ProgressPanel panel = panels.get(event.getMedia().getFile());
    event.getProgressListener().addPropertyChangeListener(ProgressListenerAdapter.TRANSFERRED, new PropertyChangeListener() {
      @Override
      public void propertyChange(final PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            final Long transferred = (Long) evt.getNewValue();
            panel.getMediaBar().setValue(transferred.intValue());
          }
        });
      }
    });
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        mediaBarsPane.getViewport().scrollRectToVisible(panel.getBounds());        
      }
    });
  }
  
  @Subscribe
  public void transferDone(final MediaTransferDoneEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        batchBar.setValue(transferCounter.incrementAndGet());
      }
    });
  }

  @Subscribe
  public void transferFailed(final MediaTransferFailedEvent event) {
    final JProgressBar mediaProgressBar = panels.get(event.getMedia().getFile()).getMediaBar();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        mediaProgressBar.setValue(mediaProgressBar.getMaximum());
        mediaProgressBar.setString(UI.BUNDLE.getString("error.generic.title"));
        mediaProgressBar.setToolTipText(MessageFormat.format(UI.BUNDLE.getString("error.upload.failed"), event.getCause().getLocalizedMessage()));
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
              mediaBarsBox.removeAll();
            }
          });
          return null;
        }
      }.execute();
    }
  }
  
  static class ProgressPanel extends Box {
    private static final long serialVersionUID = 1L;

    private final JProgressBar mediaBar;

    public ProgressPanel(final long length, final String name) {
      super(BoxLayout.Y_AXIS);
      final JLabel label = new JLabel(name);
      label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
      label.setToolTipText(name);
      this.setBackground(Color.WHITE);
      this.setOpaque(true);
      this.mediaBar = new JProgressBar(SwingConstants.HORIZONTAL);
      this.mediaBar.setStringPainted(true);
      this.mediaBar.setMaximum((int) length);
      this.mediaBar.setValue(0);
      this.mediaBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
      this.add(label);
      this.add(this.mediaBar);
      this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    }

    public JProgressBar getMediaBar() {
      return mediaBar;
    }
  }
}