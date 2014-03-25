package comeon.ui.preferences;

import java.beans.PropertyChangeListener;

import javax.swing.JList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comeon.ui.preferences.main.PreferencesController;

public abstract class SubController<M extends Model, V extends SubPanel<M>> implements ListSelectionListener, PropertyChangeListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(SubController.class);
  
  private final PreferencesController mainController;
  
  private V view;
  
  private M model;
  
  private M lastSelectedModel;
  
  protected SubController(final PreferencesController mainController) {
    this.mainController = mainController;
    this.switchToBlankModel();
  }
  
  public void setView(V view) {
    this.view = view;
    this.registerViewInterval(view);
  }
  
  protected final V getView() {
    return this.view;
  }
  
  protected final M getModel() {
    return model;
  }
  
  public final PreferencesController getMainController() {
    return mainController;
  }
  
  public final void switchToBlankModel() {
    this.switchToModel(this.makeNewModel());
  }
  
  public final void commit(final int index) {
    this.doCommit(model, lastSelectedModel, index);
  }
  
  protected abstract void doCommit(final M source, final M target, final int index);
  
  protected abstract void registerViewInterval(final V view);
  
  @SuppressWarnings("unchecked")
  public final void rollback() {
    try {
      final M newModel;
      if (lastSelectedModel == null) {
        newModel = null;
      } else {
        newModel = (M) lastSelectedModel.clone();
      }
      this.switchToModel(newModel);
    } catch (final CloneNotSupportedException e) {
      LOGGER.error("Could not clone {}", lastSelectedModel, e);
    }
  }
  
  public final void addCurrentModel() {
    this.addModel(model);
    this.switchToBlankModel();
  }
  
  private void switchToModel(final M model) {
    final M oldModel = this.model;
    if (oldModel != null) {
      oldModel.removePropertyChangeListener(this);
    }
    this.model = model;
    if (this.model != null) {
      if (this.view != null) {
        this.onModelChangedInternal(oldModel, model);
      }
      this.model.addPropertyChangeListener(this);
    }
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public final void valueChanged(final ListSelectionEvent e) {
    final JList<M> list = (JList<M>) e.getSource();
    final M selectedValue = list.getSelectedValue();
    this.lastSelectedModel = selectedValue;
    try {
      final M newModel;
      if (selectedValue == null) {
        newModel = null;
      } else {
        newModel = (M) selectedValue.clone();
      }
      this.switchToModel(newModel);
    } catch (final CloneNotSupportedException ex) {
      LOGGER.error("Could not clone {}", selectedValue, e);
    }
  }
  
  protected abstract void addModel(final M model);
  
  protected abstract M makeNewModel();
  
  public abstract void remove(final int index);
  
  protected abstract void onModelChangedInternal(final M oldModel, final M newModel);

  protected abstract class AbstractDocumentListener implements DocumentListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public final void removeUpdate(final DocumentEvent e) {
      this.update(e);
    }

    @Override
    public final void insertUpdate(final DocumentEvent e) {
      this.update(e);
    }

    @Override
    public final void changedUpdate(final DocumentEvent e) {
    }

    private void update(final DocumentEvent e) {
      try {
        this.doUpdate(getText(e));
      } catch (final BadLocationException e1) {
        logger.warn("Can't update field", e1);
      }
    }

    private String getText(final DocumentEvent e) throws BadLocationException {
      return e.getDocument().getText(0, e.getDocument().getLength());
    }
    
    protected abstract void doUpdate(final String text);

  }
}
