package comeon.ui.actions;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
  
  private final Map<String, ProgressPanel> panels;
  
  @Inject
  public TransferMonitor(final AbortAction abortAction) {
    super(null, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, UploadPicturesAction.ICON, null);
    this.getInputMap(JOptionPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed ESCAPE"), "none");
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
    this.setOptions(new Object[] { new JButton(closeAction), new JButton(abortAction) });
    this.panels = new HashMap<String, TransferMonitor.ProgressPanel>();
  }

  @Subscribe
  public void picturesAdded(final PicturesAddedEvent event) {
    for (final Picture picture : event.getPictures()) {
      final ProgressPanel panel = new ProgressPanel(picture.getFile().length(), picture.getFileName());
      panels.put(picture.getFileName(), panel);
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          pictureBarsBox.add(panel);
          final int preferredWidth = (panel.getPreferredSize().width > pictureBarsPane.getViewport().getPreferredSize().width ? panel
              .getPreferredSize().width : pictureBarsPane.getViewport().getPreferredSize().width);
          pictureBarsPane.getViewport().setPreferredSize(
              new Dimension(preferredWidth, panel.getPreferredSize().height * 3));
          TransferMonitor.this.dialog.pack();
          pictureBarsPane.getViewport().scrollRectToVisible(panel.getBounds());
        }
      });
    }
    updateBatchBarLength();
  }

  @Subscribe
  public void pictureRemoved(final PictureRemovedEvent event) {
    panels.remove(event.getPicture().getFileName());
    updateBatchBarLength();
  }
  
  private void updateBatchBarLength() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        TransferMonitor.this.batchBar.setMaximum(panels.size());
      }
    });
  }
  
  @Subscribe
  public void uploadStarting(final UploadStartingEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        TransferMonitor.this.dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TransferMonitor.this.closeAction.setEnabled(false);
        TransferMonitor.this.batchBar.setValue(0);
        TransferMonitor.this.dialog.setVisible(true);
      }
    });
  }

  @Subscribe
  public void transferStarting(final PictureTransferStartingEvent event) {
    final ProgressPanel panel = panels.get(event.getPicture().getFileName());
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
  }
  
  @Subscribe
  public void transferDone(final PictureTransferDoneEvent event) {
    incrementBatchBar();
  }

  @Subscribe
  public void transferFailed(final PictureTransferFailedEvent event) {
    incrementBatchBar();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        final JProgressBar pictureProgressBar = panels.get(event.getPicture().getFileName()).getPictureBar();
        pictureProgressBar.setValue(pictureProgressBar.getMaximum());
        pictureProgressBar.setString(UI.BUNDLE.getString("error.generic.title"));
        pictureProgressBar.setToolTipText(MessageFormat.format(UI.BUNDLE.getString("error.upload.failed"), event.getCause().getLocalizedMessage()));
      }
    });
  }
  
  private void incrementBatchBar() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        TransferMonitor.this.batchBar.setValue(batchBar.getValue() + 1);
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
      dialog.setVisible(false);
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