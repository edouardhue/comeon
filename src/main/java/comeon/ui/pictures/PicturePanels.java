package comeon.ui.pictures;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;

import comeon.model.Picture;
import comeon.ui.UI;

public final class PicturePanels {
  private static final Logger LOGGER = LoggerFactory.getLogger(PicturePanels.class);

  private final Picture picture;

  private BufferedImage image;

  private final PicturePreviewPanel previewPanel;

  private final PictureEditPanel editPanel;

  public PicturePanels(final Picture picture) {
    this.picture = picture;
    final ByteArrayInputStream input = new ByteArrayInputStream(picture.getThumbnail());
    try {
      try {
        final BufferedImage pictureThumbnail = ImageIO.read(input);
        if (pictureThumbnail == null) {
          this.image = ImageIO.read(Resources.getResource("comeon/ui/default_thumbnail.png"));
        } else {
          this.image = pictureThumbnail;
        }
      } finally {
        input.close();
      }
    } catch (final IOException e) {
      LOGGER.warn("Can't load picture thumbnail {}", picture.getFileName(), e);
    }
    this.previewPanel = new PicturePreviewPanel(this, ConstrainedAxis.VERTICAL.getPreviewPanelDimension(image,
        UI.PREVIEW_PANEL_HEIGHT), 2, 4);
    this.editPanel = new PictureEditPanel(this);
  }

  Picture getPicture() {
    return picture;
  }

  BufferedImage getImage() {
    return image;
  }

  public PicturePreviewPanel getPreviewPanel() {
    return previewPanel;
  }

  public PictureEditPanel getEditPanel() {
    return editPanel;
  }
}
