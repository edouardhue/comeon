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
//    this.picture = picture;
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
    this.previewPanel = new PicturePreviewPanel();
  }
  
  public PicturePreviewPanel getPreviewPanel() {
    return previewPanel;
  }
  
  final class PicturePreviewPanel extends JComponent {

    private static final long serialVersionUID = 1L;
    
    private static final int V_INSETS = 8;
    
    private static final int H_INSETS = 4;
    
    private static final int BORDER_WIDTH = 1;
    
    private static final int THUMB_HEIGHT = 90;
    
    public static final int COMPONENT_HEIGHT = THUMB_HEIGHT + (V_INSETS * 2);
    
    public PicturePreviewPanel() {
      // Minimum size is preferred size is maximum size is thumbnail size + 10 px around
      final Dimension componentSize = new Dimension((int) (image.getWidth() * ((double) COMPONENT_HEIGHT / image.getHeight())), COMPONENT_HEIGHT);
      super.setMinimumSize(componentSize);
      super.setPreferredSize(componentSize);
      super.setMaximumSize(componentSize);
      super.setBackground(UI.NEUTRAL_GREY);
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
      final Dimension size = this.getSize();
      final Graphics2D g2 = (Graphics2D) g;
      final int componentWidth = (int) size.getWidth();
      final int componentHeight = (int) size.getHeight();
      g2.setColor(Color.WHITE);
      g2.fillRect(H_INSETS - BORDER_WIDTH, V_INSETS - BORDER_WIDTH, componentWidth - (H_INSETS * 2) + (BORDER_WIDTH * 2), componentHeight - (V_INSETS * 2) + (BORDER_WIDTH * 2));
      g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2.drawImage(image, H_INSETS, V_INSETS, componentWidth - H_INSETS, componentHeight - V_INSETS, 0, 0, image.getWidth(), image.getHeight(), getBackground(), null);
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
