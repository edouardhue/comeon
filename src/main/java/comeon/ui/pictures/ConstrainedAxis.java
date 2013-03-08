package comeon.ui.pictures;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

enum ConstrainedAxis {
  VERTICAL {
    @Override
    Dimension getPreviewPanelDimension(final BufferedImage image, final int desiredSize) {
      return new Dimension((int) ((double) image.getWidth() * ((double) desiredSize / (double) image.getHeight())), desiredSize);
    }
  },
  HORIZONTAL {
    @Override
    Dimension getPreviewPanelDimension(final BufferedImage image, final int desiredSize) {
      return new Dimension(desiredSize, (int) ((double) image.getHeight() * ((double) desiredSize / (double) image.getWidth())));
    }
  };
  
  abstract Dimension getPreviewPanelDimension(BufferedImage image, int desiredSize);
}