package comeon.ui.preferences.wikis;

import java.beans.PropertyChangeEvent;

import comeon.ui.preferences.PreferencesController;
import comeon.ui.preferences.SubController;

public final class WikiSubController extends SubController<WikiModel, WikiSubPanel> {

  public WikiSubController(final PreferencesController mainController) {
    super(mainController);
  }

  @Override
  protected void registerViewInterval(final WikiSubPanel view) {
    view.getNameField().getDocument().addDocumentListener(new AbstractDocumentListener() {
      @Override
      protected void doUpdate(final String text) {
        getModel().setName(text);
      }
    });
    view.getUrlField().getDocument().addDocumentListener(new AbstractDocumentListener() {
      @Override
      protected void doUpdate(final String text) {
        getModel().setUrl(text);
      }
    });
    view.getDisplayNameField().getDocument().addDocumentListener(new AbstractDocumentListener() {
      @Override
      protected void doUpdate(final String text) {
        getModel().setDisplayName(text);
      }
    });
    view.getLoginField().getDocument().addDocumentListener(new AbstractDocumentListener() {
      @Override
      protected void doUpdate(final String text) {
        getModel().setLogin(text);
      }
    });
    view.getPasswordField().getDocument().addDocumentListener(new AbstractDocumentListener() {
      @Override
      protected void doUpdate(final String text) {
        getModel().setPassword(text);
      }
    });
  }
  
  @Override
  public void propertyChange(final PropertyChangeEvent evt) {
    // Noop
  }

  @Override
  protected void doCommit(final WikiModel source, final WikiModel target, final int index) {
    target.setName(source.getName());
    target.setUrl(source.getUrl());
    target.setDisplayName(source.getDisplayName());
    target.setLogin(source.getLogin());
    target.setPassword(source.getPassword());
    getMainController().update(target, index);
  }

  @Override
  protected void onModelChangedInternal(final WikiModel oldModel, final WikiModel newModel) {
    getView().getNameField().setText(newModel.getName());
    getView().getUrlField().setText(newModel.getUrl());
    getView().getDisplayNameField().setText(newModel.getDisplayName());
    getView().getLoginField().setText(newModel.getLogin());
    getView().getPasswordField().setText(newModel.getPassword());
  }

  @Override
  protected WikiModel makeNewModel() {
    return new WikiModel();
  }
  
  @Override
  protected void addModel(final WikiModel model) {
    getMainController().add(model);
  }
  
  @Override
  public void remove(final int index) {
    getMainController().removeWiki(index);
  }

}
