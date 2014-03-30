package comeon.ui.preferences.wikis;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.google.common.io.Resources;
import comeon.ui.preferences.BaseListCellRenderer;

public final class WikiListCellRenderer extends BaseListCellRenderer<WikiModel> {
  private static final long serialVersionUID = 1L;
  
  private static final String TEMPLATE = "<html><strong>%1$s</strong><br/><em>%2$s</em></html>";
  
  private static final Icon ACTIVE_ICON = new ImageIcon(Resources.getResource("comeon/ui/active.png"));

  @Override
  protected void customizeComponent(final WikiModel wiki) {
    setText(String.format(TEMPLATE, wiki.getName(), wiki.getDisplayName()));
    if (wiki.getActive()) {
      setIcon(ACTIVE_ICON);
    }
  }

}
