package comeon.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.Core;
import comeon.core.events.UploadDoneEvent;
import comeon.core.events.UploadStartingEvent;
import comeon.ui.UI;

@Singleton
public final class AbortAction extends AbstractAction {
  private static final long serialVersionUID = 1L;
  
  private final Core core;
  
  @Inject
  public AbortAction(final Core core) {
    super(UI.BUNDLE.getString("upload.abort"));
    this.core = core;
  }
  
  @Override
  public void actionPerformed(final ActionEvent e) {
    new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        core.abort();
        return null;
      }
    }.execute();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        setEnabled(false);
      }
    });
  }
  
  @Subscribe
  public void uploadStarting(final UploadStartingEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        setEnabled(true);
      }
    });
  }
  
  @Subscribe
  public void uploadDone(final UploadDoneEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        setEnabled(false);
      }
    });
  }
}