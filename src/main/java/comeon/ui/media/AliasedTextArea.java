package comeon.ui.media;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

final class AliasedTextArea extends JTextArea {
  private static final long serialVersionUID = 1L;

  AliasedTextArea(final String text) {
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