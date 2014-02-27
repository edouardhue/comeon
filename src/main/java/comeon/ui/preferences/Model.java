package comeon.ui.preferences;

import java.beans.PropertyChangeListener;

interface Model {

  void addPropertyChangeListener(final PropertyChangeListener pcl);
  
  void removePropertyChangeListener(final PropertyChangeListener pcl);
}
