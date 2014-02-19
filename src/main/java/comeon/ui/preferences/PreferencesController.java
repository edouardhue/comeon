package comeon.ui.preferences;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public final class PreferencesController {
  
  private PreferencesModel model;
  
  public PreferencesController() {
  }
  
  public void registerModel(final PreferencesModel model) {
    this.model = model;
    this.model.getTemplates().addListDataListener(new ListDataListener() {
      
      @Override
      public void intervalRemoved(ListDataEvent e) {
        
      }
      
      @Override
      public void intervalAdded(ListDataEvent e) {
        
      }
      
      @Override
      public void contentsChanged(ListDataEvent e) {
        
      }
    });
    
    this.model.getWikis().addListDataListener(new ListDataListener() {
      
      @Override
      public void intervalRemoved(ListDataEvent e) {
        
      }
      
      @Override
      public void intervalAdded(ListDataEvent e) {
        
      }
      
      @Override
      public void contentsChanged(ListDataEvent e) {
        
      }
    });
  }

  public ListModel<TemplateModel> getTemplates() {
    return model.getTemplates();
  }

  public ListModel<WikiModel> getWikis() {
    return model.getWikis();
  }
  
  
}
