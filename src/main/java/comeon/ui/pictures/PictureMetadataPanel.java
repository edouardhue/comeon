package comeon.ui.pictures;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.beanutils.DynaBean;

import comeon.ui.UI;

final class PictureMetadataPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  
  public static final int PREVIEW_WIDTH = (int) (UI.METADATA_PANEL_WIDTH * 0.9);

  public PictureMetadataPanel(final PicturePanels panels) {
    super(new BorderLayout());
    this.setMinimumSize(new Dimension(UI.METADATA_PANEL_WIDTH, 0));
    this.setMaximumSize(new Dimension(UI.METADATA_PANEL_WIDTH, Integer.MAX_VALUE));
    this.setBackground(UI.NEUTRAL_GREY);
    this.setOpaque(true);
    final Dimension previewPanelDimension = new Dimension(UI.METADATA_PANEL_WIDTH, UI.METADATA_PANEL_WIDTH);
    final Dimension previewDimension;
    if (panels.getImage().getWidth() >= panels.getImage().getHeight()) {
      previewDimension = ConstrainedAxis.HORIZONTAL.getPreviewPanelDimension(panels.getImage(), PREVIEW_WIDTH);
    } else {
      previewDimension = ConstrainedAxis.VERTICAL.getPreviewPanelDimension(panels.getImage(), PREVIEW_WIDTH);
    }
    final PicturePreviewPanel previewPanel = new PicturePreviewPanel(panels, previewPanelDimension,
        (previewPanelDimension.width - previewDimension.width) / 2,
        (previewPanelDimension.height - previewDimension.height) / 2);
    this.add(previewPanel, BorderLayout.NORTH);
    final Box metadataBox = new Box(BoxLayout.Y_AXIS);
    for (final Map.Entry<String, Object> dir : panels.getPicture().getMetadata().entrySet()) {
      if (dir.getValue() instanceof DynaBean) {
        final MetadataTable table = new MetadataTable(dir.getKey(), (DynaBean) dir.getValue());
        metadataBox.add(table);
      }
    }
    final JScrollPane metadataScrollPane = new JScrollPane(metadataBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    this.add(metadataScrollPane, BorderLayout.CENTER);
  }
}