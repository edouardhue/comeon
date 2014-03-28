package comeon.ui.pictures;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

import comeon.model.Picture;
import comeon.model.Picture.State;

final class PicturePreviewPanel extends JComponent implements PropertyChangeListener {

  private static final long serialVersionUID = 1L;

  private static final int INNER_BORDER_WIDTH = 1;
  
  private final Map<Picture.State, Border> borders;

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
    this.borders = new EnumMap<>(State.class);
    final Border outerBorder = BorderFactory.createEmptyBorder(verticalBordersWidth, horizontalBordersWidth, verticalBordersWidth, horizontalBordersWidth);
    borders.put(State.ToBeUploaded, BorderFactory.createCompoundBorder(outerBorder, BorderFactory.createLineBorder(Color.WHITE, INNER_BORDER_WIDTH)));
    borders.put(State.UploadedSuccessfully, BorderFactory.createCompoundBorder(outerBorder, BorderFactory.createLineBorder(Color.GREEN, INNER_BORDER_WIDTH)));
    borders.put(State.FailedUpload, BorderFactory.createCompoundBorder(outerBorder, BorderFactory.createLineBorder(Color.RED, INNER_BORDER_WIDTH)));
    this.setBorder(borders.get(picturePanels.getPicture().getState()));
    picturePanels.getPicture().addPropertyChangeListener(this);
  }

  @Override
  protected void paintComponent(final Graphics g) {
    switch (this.picturePanels.getPicture().getState()) {
    case ToBeUploaded:
      this.setOpaque(false);
      this.setBackground(Color.BLUE);
      break;
    case UploadedSuccessfully:
      this.setOpaque(true);
      this.setBackground(Color.GREEN);
      break;
    case FailedUpload:
      this.setOpaque(true);
      this.setBackground(Color.RED);
      break;
    }
    
    final Dimension size = this.getSize();
    final Graphics2D g2 = (Graphics2D) g;
    final int componentWidth = (int) size.getWidth();
    final int componentHeight = (int) size.getHeight();
    g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2.drawImage(this.picturePanels.getImage(), horizontalBordersWidth + INNER_BORDER_WIDTH, verticalBordersWidth
        + INNER_BORDER_WIDTH, componentWidth - (horizontalBordersWidth + INNER_BORDER_WIDTH), componentHeight
        - (verticalBordersWidth + INNER_BORDER_WIDTH), 0, 0, this.picturePanels.getImage().getWidth(), this.picturePanels
        .getImage().getHeight(), getBackground(), null);
  }

  @Override
  public void propertyChange(final PropertyChangeEvent evt) {
    if (evt.getSource() instanceof Picture && "state".equals(evt.getPropertyName())) {
      final State newState = (State) evt.getNewValue();
      this.setBorder(borders.get(newState));
      this.repaint();
    }
    
  }
}