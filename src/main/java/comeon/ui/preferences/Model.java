package comeon.ui.preferences;

import java.beans.PropertyChangeListener;

interface Model extends Cloneable {

  void addPropertyChangeListener(final PropertyChangeListener pcl);
  
  void removePropertyChangeListener(final PropertyChangeListener pcl);
  
  Object clone() throws CloneNotSupportedException;
}
