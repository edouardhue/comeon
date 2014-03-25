package comeon.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import comeon.ui.UI;
import comeon.ui.preferences.main.PreferencesController;
import comeon.ui.preferences.main.PreferencesDialog;
import comeon.ui.preferences.main.PreferencesSavingException;

@Singleton
public final class PreferencesAction extends BaseAction {
  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(PreferencesAction.class);

  private final PreferencesDialog dialog;

  private final PreferencesController controller;
  
  @Inject
  public PreferencesAction(final PreferencesDialog dialog, final PreferencesController controller) {
    super("preferences");
    this.dialog = dialog;
    this.controller = controller;
  }

  @Override
  public void actionPerformed(final ActionEvent evt) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        final int value = dialog.showDialog();
        if (value == JOptionPane.OK_OPTION) {
          try {
            controller.persist();
          } catch (final PreferencesSavingException e) {
            final List<Exception> causes = e.getCauses();
            final StringBuilder causeMessage = new StringBuilder(UI.BUNDLE.getString("prefs.error.save"));
            for (final Exception c : causes) {
              LOGGER.warn("Could not save preferences", c);
              causeMessage.append("\n* ");
              causeMessage.append(c.getLocalizedMessage());
            }
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), causeMessage.toString(), UI.BUNDLE.getString("error.generic.title"), JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    });
  }

}
