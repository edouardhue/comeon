package comeon.ui.preferences.templates;

import comeon.ui.preferences.BaseListCellRenderer;

public final class TemplateListCellRenderer extends BaseListCellRenderer<TemplateModel> {

    private static final long serialVersionUID = 1L;

    private static final String TEMPLATE = "<html><strong>%1$s</strong><br/><em>%2$s</em></html>";

    @Override
    protected void customizeComponent(final TemplateModel template) {
        setText(String.format(TEMPLATE, template.getName(), template.getDescription()));
    }

}
