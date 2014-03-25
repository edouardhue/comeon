package comeon.ui.preferences.main;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ui.preferences.wikis.WikiListCellRenderer;
import comeon.ui.preferences.wikis.WikiModel;
import comeon.ui.preferences.wikis.WikiSubController;
import comeon.ui.preferences.wikis.WikiSubPanel;

@Singleton
public final class WikisListPanel extends ListPanel<WikiModel> {
  private static final long serialVersionUID = 1L;

  @Inject
  public WikisListPanel(final WikiSubController subController,
      final WikiSubPanel subPanel) {
    super(new WikiListCellRenderer(), subController, subPanel, subController.getMainController().getWikis(), "wikis", WikiModel.getPrototype());
  }
}