package comeon.ui.preferences;

import java.beans.PropertyChangeListener;

import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;

abstract class SubController<M extends Model, V extends SubPanel<M>> implements ListSelectionListener, PropertyChangeListener {
  private final PreferencesController mainController;
  
  private V view;
  
  private M model;
  
  protected SubController(final PreferencesController mainController) {
    this.mainController = mainController;
  }
  
  public final void registerView(final V view) {
    this.view = view;
  }
  
  protected final V getView() {
    return this.view;
  }
  
  protected final M getModel() {
    return model;
  }
  
  protected final PreferencesController getMainController() {
    return mainController;
  }
  
  private void registerModel(final M model) {
    final M oldModel = this.model;
    this.model = model;
    onModelChanged(oldModel, model);
  }
  
  @Override
  public final void valueChanged(final ListSelectionEvent e) {
    @SuppressWarnings("unchecked")
    final JList<M> list = (JList<M>) e.getSource();
    this.model = list.getSelectedValue();
    this.registerModel(model);
  }
  
  public abstract void remove(final int index);
  
  private void onModelChanged(final M oldModel, final M newModel) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        oldModel.removePropertyChangeListener(SubController.this);
        onModelChangedInternal(oldModel, newModel);
        newModel.addPropertyChangeListener(SubController.this);
      }
    });
  }
  
  protected abstract void registerViewInterval(final V view);
  
  protected abstract void onModelChangedInternal(final M oldModel, final M newModel);

  protected abstract class AbstractDocumentListener implements DocumentListener {

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
      }
    }

    private String getText(final DocumentEvent e) throws BadLocationException {
      return e.getDocument().getText(0, e.getDocument().getLength());
    }
    
    protected abstract void doUpdate(final String text);

  }
}
