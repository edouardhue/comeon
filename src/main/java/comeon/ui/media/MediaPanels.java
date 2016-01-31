package comeon.ui.media;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;

import comeon.model.Media;
import comeon.ui.UI;

public final class MediaPanels {
  private static final Logger LOGGER = LoggerFactory.getLogger(MediaPanels.class);

  private final Media media;

  private BufferedImage thumbnail;

  private final MediaPreviewPanel previewPanel;

  private final MediaEditPanel editPanel;

  public MediaPanels(final Media media) {
    this.media = media;
    final ByteArrayInputStream input = new ByteArrayInputStream(media.getThumbnail());
    try {
      try {
        final BufferedImage mediaThumbnail = ImageIO.read(input);
        if (mediaThumbnail == null) {
          this.thumbnail = ImageIO.read(Resources.getResource("comeon/ui/default_thumbnail.png"));
        } else {
          this.thumbnail = mediaThumbnail;
        }
      } finally {
        input.close();
      }
    } catch (final IOException e) {
      LOGGER.warn("Can't load media thumbnail {}", media.getFileName(), e);
    }
    this.previewPanel = new MediaPreviewPanel(this, ConstrainedAxis.VERTICAL.getPreviewPanelDimension(thumbnail,
        UI.PREVIEW_PANEL_HEIGHT), 2, 4);
    this.editPanel = new MediaEditPanel(this);
  }

  Media getMedia() {
    return media;
  }

  BufferedImage getThumbnail() {
    return thumbnail;
  }

  public MediaPreviewPanel getPreviewPanel() {
    return previewPanel;
  }

  public MediaEditPanel getEditPanel() {
    return editPanel;
  }
}
