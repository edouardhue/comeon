package comeon.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comeon.model.Picture;

final class PicturePanels {
  private static final Logger LOGGER = LoggerFactory.getLogger(PicturePanels.class);
  
//  private final Picture picture;

  private BufferedImage image;
  
  private final PicturePreviewPanel previewPanel;
  
  public PicturePanels(final Picture picture) {
    final ByteArrayInputStream input = new ByteArrayInputStream(picture.getThumbnail());
    try {
      try {
        this.image = ImageIO.read(input);
      } finally {
        input.close();
      }
    } catch (final IOException e) {
      LOGGER.warn("Can't load picture thumbnail {}", picture.getFileName(), e);
    }
    this.previewPanel = new PicturePreviewPanel(UI.PREVIEW_PANEL_HEIGHT, 2, 4);
  }
  
  public PicturePreviewPanel getPreviewPanel() {
    return previewPanel;
  }
  
  final class PicturePreviewPanel extends JComponent {

    private static final long serialVersionUID = 1L;
    
    private static final int WHITE_LINE_WIDTH = 1;
    
    private final int horizontalBordersWidth;
    
    private final int verticalBordersWidth;
    
    public PicturePreviewPanel(final int thumbHeight, final int horizontalBordersWidth, final int verticalBordersWidth) {
      final Dimension componentSize = new Dimension((int) (image.getWidth() * ((double) thumbHeight / image.getHeight())), thumbHeight);
      super.setMinimumSize(componentSize);
      super.setPreferredSize(componentSize);
      super.setMaximumSize(componentSize);
      this.horizontalBordersWidth = horizontalBordersWidth;
      this.verticalBordersWidth = verticalBordersWidth;
      this.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createEmptyBorder(verticalBordersWidth, horizontalBordersWidth, verticalBordersWidth, horizontalBordersWidth),
          BorderFactory.createLineBorder(Color.WHITE, WHITE_LINE_WIDTH)));
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
      final Dimension size = this.getSize();
      final Graphics2D g2 = (Graphics2D) g;
      final int componentWidth = (int) size.getWidth();
      final int componentHeight = (int) size.getHeight();
      g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2.drawImage(image,
          horizontalBordersWidth + WHITE_LINE_WIDTH, verticalBordersWidth + WHITE_LINE_WIDTH,
          componentWidth - (horizontalBordersWidth + WHITE_LINE_WIDTH), componentHeight - (verticalBordersWidth + WHITE_LINE_WIDTH),
          0, 0, image.getWidth(), image.getHeight(),
          getBackground(), null);
    }
  }
  
  final class PictureEditPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public PictureEditPanel() {
      super(new BorderLayout());
    }
  }
  
  final class PictureMetadataPanel extends Box {

    private static final long serialVersionUID = 1L;
    
    
    public PictureMetadataPanel() {
      super(BoxLayout.Y_AXIS);
    }
  }
}
