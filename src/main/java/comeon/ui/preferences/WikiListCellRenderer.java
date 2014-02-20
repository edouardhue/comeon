package comeon.ui.preferences;

final class WikiListCellRenderer extends BaseListCellRenderer<WikiModel> {
  private static final long serialVersionUID = 1L;
  
  private static final String TEMPLATE = "<html><strong>%1$s</strong><br/><em>%2$s</em></html>";
  

  @Override
  protected void customizeComponent(final WikiModel wiki) {
    setText(String.format(TEMPLATE, wiki.getName(), wiki.getDisplayName()));
  }

}
