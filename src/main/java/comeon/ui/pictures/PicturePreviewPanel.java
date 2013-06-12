package comeon.ui.pictures;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

final class PicturePreviewPanel extends JComponent {

  private static final long serialVersionUID = 1L;

  private static final int WHITE_LINE_WIDTH = 1;

  private final PicturePanels picturePanels;

  private final int horizontalBordersWidth;

  private final int verticalBordersWidth;

  public PicturePreviewPanel(final PicturePanels picturePanels, final Dimension componentSize,
      final int horizontalBordersWidth, final int verticalBordersWidth) {
    super.setMinimumSize(componentSize);
    super.setPreferredSize(componentSize);
    super.setMaximumSize(componentSize);
    this.picturePanels = picturePanels;
    this.horizontalBordersWidth = horizontalBordersWidth;
    this.verticalBordersWidth = verticalBordersWidth;
    this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(verticalBordersWidth,
        horizontalBordersWidth, verticalBordersWidth, horizontalBordersWidth), BorderFactory.createLineBorder(
        Color.WHITE, WHITE_LINE_WIDTH)));
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
    g2.drawImage(this.picturePanels.getImage(), horizontalBordersWidth + WHITE_LINE_WIDTH, verticalBordersWidth
        + WHITE_LINE_WIDTH, componentWidth - (horizontalBordersWidth + WHITE_LINE_WIDTH), componentHeight
        - (verticalBordersWidth + WHITE_LINE_WIDTH), 0, 0, this.picturePanels.getImage().getWidth(), this.picturePanels
        .getImage().getHeight(), getBackground(), null);
  }
}