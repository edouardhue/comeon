package comeon.ui.media;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.apache.commons.beanutils.DynaBean;

import comeon.core.Core;
import comeon.ui.UI;
import comeon.ui.media.metadata.ExternalMetadataTable;
import comeon.ui.media.metadata.OtherMetadataTable;
import comeon.ui.media.metadata.MediaMetadataTable;

final class MediaMetadataPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final int PREVIEW_WIDTH = (int) (UI.METADATA_PANEL_WIDTH * 0.9);

  public MediaMetadataPanel(final MediaPanels panels) {
    super(new BorderLayout());
    this.setMinimumSize(new Dimension(UI.METADATA_PANEL_WIDTH, 0));
    this.setMaximumSize(new Dimension(UI.METADATA_PANEL_WIDTH, Integer.MAX_VALUE));
    this.setBackground(UI.NEUTRAL_GREY);
    this.setOpaque(true);
    final Dimension previewPanelDimension = new Dimension(UI.METADATA_PANEL_WIDTH, UI.METADATA_PANEL_WIDTH);
    final Dimension previewDimension;
    if (panels.getThumbnail().getWidth() >= panels.getThumbnail().getHeight()) {
      previewDimension = ConstrainedAxis.HORIZONTAL.getPreviewPanelDimension(panels.getThumbnail(), PREVIEW_WIDTH);
    } else {
      previewDimension = ConstrainedAxis.VERTICAL.getPreviewPanelDimension(panels.getThumbnail(), PREVIEW_WIDTH);
    }
    final MediaPreviewPanel previewPanel = new MediaPreviewPanel(panels, previewPanelDimension,
        (previewPanelDimension.width - previewDimension.width) / 2,
        (previewPanelDimension.height - previewDimension.height) / 2);
    this.add(previewPanel, BorderLayout.NORTH);
    final Box metadataBox = new Box(BoxLayout.Y_AXIS);
    final Map<String, Object> otherMetadata = new HashMap<>();
    for (final Map.Entry<String, Object> dir : panels.getMedia().getMetadata().entrySet()) {
      if (dir.getValue() instanceof DynaBean) {
        final MediaMetadataTable table = new MediaMetadataTable(dir.getKey(), (DynaBean) dir.getValue());
        metadataBox.add(table, 0);
      } else if (Core.EXTERNAL_METADATA_KEY.equals(dir.getKey())) {
        final ExternalMetadataTable table = new ExternalMetadataTable(UI.BUNDLE.getString("media.metadata.external"), dir.getValue());
        metadataBox.add(table);
      } else {
        otherMetadata.put(dir.getKey(), dir.getValue());
      }
    }
    if (!otherMetadata.isEmpty()) {
      metadataBox.add(new OtherMetadataTable(UI.BUNDLE.getString("media.metadata.other"), otherMetadata));
    }
    
    final JScrollPane metadataScrollPane = new JScrollPane(metadataBox,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    this.add(metadataScrollPane, BorderLayout.CENTER);
  }
}