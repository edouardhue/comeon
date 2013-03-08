package comeon.ui.pictures;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;

import com.drew.metadata.Directory;
import comeon.ui.UI;

final class PictureMetadataPanel extends Box {

  private static final long serialVersionUID = 1L;
  
  public PictureMetadataPanel(final PicturePanels panels) {
    super(BoxLayout.Y_AXIS);
    this.setMinimumSize(new Dimension(UI.METADATA_PANEL_WIDTH, 0));
    this.setMaximumSize(new Dimension(UI.METADATA_PANEL_WIDTH, Integer.MAX_VALUE));
    this.setBackground(UI.NEUTRAL_GREY);
    this.setOpaque(true);
    this.add(new PicturePreviewPanel(panels, ConstrainedAxis.HORIZONTAL.getPreviewPanelDimension(panels.getImage(), UI.METADATA_PANEL_WIDTH), 8, 8));
    for (final Directory dir : panels.getPicture().getMetadata().getDirectories()) {
      final MetadataTable table = new MetadataTable(dir);
      this.add(table);
    }
    this.add(Box.createVerticalGlue());
  }
}