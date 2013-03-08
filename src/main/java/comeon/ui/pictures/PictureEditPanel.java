package comeon.ui.pictures;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

final class PictureEditPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  public PictureEditPanel(final PicturePanels panels) {
    super(new BorderLayout());
    this.add(new PictureMetadataPanel(panels), BorderLayout.WEST);
    
    final JTextArea templateText = new AliasedTextArea(panels.getPicture().getTemplateText());
    final JTextArea renderedTemplate = new AliasedTextArea(panels.getPicture().getRenderedTemplate());

    final JSplitPane templatesPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, new JScrollPane(templateText), new JScrollPane(renderedTemplate));
    this.add(templatesPanel, BorderLayout.CENTER);
  }
}