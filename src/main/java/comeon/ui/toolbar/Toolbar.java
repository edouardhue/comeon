package comeon.ui.toolbar;

import javax.swing.JToolBar;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.actions.AddPicturesAction;
import comeon.ui.actions.HelpManualAction;
import comeon.ui.actions.PreferencesAction;
import comeon.ui.actions.UploadPicturesAction;

@Singleton
public final class Toolbar extends JToolBar {
  
  private static final long serialVersionUID = 1L;

  @Inject
  public Toolbar(final AddPicturesAction addPicturesAction, final UploadPicturesAction uploadPicturesAction, final PreferencesAction preferencesAction, final HelpManualAction helpManualAction) {
    super(JToolBar.HORIZONTAL);
    this.setFloatable(false);
    
    this.add(addPicturesAction);
    this.add(uploadPicturesAction);
    
    this.addSeparator();
    
    this.add(preferencesAction);
    
    this.addSeparator();
    
    this.add(helpManualAction);
  }
}
