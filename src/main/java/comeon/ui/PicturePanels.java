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
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comeon.model.Picture;

final class PicturePanels {
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
        this.image = ImageIO.read(input);
      } finally {
        input.close();
      }
    } catch (final IOException e) {
      LOGGER.warn("Can't load picture thumbnail {}", picture.getFileName(), e);
    }
    this.previewPanel = new PicturePreviewPanel(ConstrainedAxis.VERTICAL.getPreviewPanelDimension(image, UI.PREVIEW_PANEL_HEIGHT), 2, 4);
    this.editPanel = new PictureEditPanel();
  }
  
  public PicturePreviewPanel getPreviewPanel() {
    return previewPanel;
  }
  
  public PictureEditPanel getEditPanel() {
    return editPanel;
  }
  
  private enum ConstrainedAxis {
    VERTICAL {
      @Override
      Dimension getPreviewPanelDimension(final BufferedImage image, final int desiredSize) {
        return new Dimension((int) (image.getWidth() * ((double) desiredSize / image.getHeight())), desiredSize);
      }
    },
    HORIZONTAL {
      @Override
      Dimension getPreviewPanelDimension(final BufferedImage image, final int desiredSize) {
        return new Dimension(desiredSize, (int) (image.getHeight() * ((double) desiredSize / image.getWidth())));
      }
    };
    
    abstract Dimension getPreviewPanelDimension(BufferedImage image, int desiredSize);
  }
  
  final class PicturePreviewPanel extends JComponent {

    private static final long serialVersionUID = 1L;
    
    private static final int WHITE_LINE_WIDTH = 1;
    
    private final int horizontalBordersWidth;
    
    private final int verticalBordersWidth;
    
    public PicturePreviewPanel(final Dimension componentSize, final int horizontalBordersWidth, final int verticalBordersWidth) {
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
      this.add(new PictureMetadataPanel(), BorderLayout.WEST);
      
      final JTextArea templateText = new AliasedTextArea(picture.getTemplateText());
      final JTextArea renderedTemplate = new AliasedTextArea(picture.getRenderedTemplate());

      final JSplitPane templatesPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, new JScrollPane(templateText), new JScrollPane(renderedTemplate));
      this.add(templatesPanel, BorderLayout.CENTER);
    }
  }
  
  private static final class AliasedTextArea extends JTextArea {
    private static final long serialVersionUID = 1L;

    private AliasedTextArea(final String text) {
      super(text);
      this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    }

    @Override
    protected void paintComponent(final Graphics g) {
      final Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
      g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
      g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      super.paintComponent(g);
    }
  }
  
  final class PictureMetadataPanel extends Box {

    private static final long serialVersionUID = 1L;
    
    public PictureMetadataPanel() {
      super(BoxLayout.Y_AXIS);
      this.setMinimumSize(new Dimension(UI.METADATA_PANEL_WIDTH, 0));
      this.setMaximumSize(new Dimension(UI.METADATA_PANEL_WIDTH, Integer.MAX_VALUE));
      this.setBackground(UI.NEUTRAL_GREY);
      this.setOpaque(true);
      this.add(new PicturePreviewPanel(ConstrainedAxis.HORIZONTAL.getPreviewPanelDimension(image, UI.METADATA_PANEL_WIDTH), 16, 16));
      this.add(Box.createVerticalGlue());
    }
  }
}
