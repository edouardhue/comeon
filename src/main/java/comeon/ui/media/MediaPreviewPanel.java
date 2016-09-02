package comeon.ui.media;

import comeon.model.Media;
import comeon.model.Media.State;
import comeon.ui.UI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumMap;
import java.util.Map;

final class MediaPreviewPanel extends JComponent implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;

    private static final int INNER_BORDER_WIDTH = 1;

    private final Map<Media.State, Border> borders;

    private final MediaPanels mediaPanels;

    private final int horizontalBordersWidth;

    private final int verticalBordersWidth;

    public MediaPreviewPanel(final MediaPanels mediaPanels, final Dimension componentSize,
                             final int horizontalBordersWidth, final int verticalBordersWidth) {
        super.setMinimumSize(componentSize);
        super.setPreferredSize(componentSize);
        super.setMaximumSize(componentSize);
        this.setToolTipText(UI.BUNDLE.getString("media.preview.tooltip"));
        this.setBackground(Color.LIGHT_GRAY);
        this.mediaPanels = mediaPanels;
        this.horizontalBordersWidth = horizontalBordersWidth;
        this.verticalBordersWidth = verticalBordersWidth;
        this.borders = new EnumMap<>(State.class);
        final Border outerBorder = BorderFactory.createEmptyBorder(verticalBordersWidth, horizontalBordersWidth, verticalBordersWidth, horizontalBordersWidth);
        borders.put(State.ToBeUploaded, BorderFactory.createCompoundBorder(outerBorder, BorderFactory.createLineBorder(Color.WHITE, INNER_BORDER_WIDTH)));
        borders.put(State.UploadedSuccessfully, BorderFactory.createCompoundBorder(outerBorder, BorderFactory.createLineBorder(Color.GREEN, INNER_BORDER_WIDTH)));
        borders.put(State.FailedUpload, BorderFactory.createCompoundBorder(outerBorder, BorderFactory.createLineBorder(Color.RED, INNER_BORDER_WIDTH)));
        this.setBorder(borders.get(mediaPanels.getMedia().getState()));
        mediaPanels.getMedia().addPropertyChangeListener(this);
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
        g2.drawImage(this.mediaPanels.getThumbnail(), horizontalBordersWidth + INNER_BORDER_WIDTH, verticalBordersWidth
                + INNER_BORDER_WIDTH, componentWidth - (horizontalBordersWidth + INNER_BORDER_WIDTH), componentHeight
                - (verticalBordersWidth + INNER_BORDER_WIDTH), 0, 0, this.mediaPanels.getThumbnail().getWidth(), this.mediaPanels
                .getThumbnail().getHeight(), getBackground(), null);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getSource() instanceof Media && "state".equals(evt.getPropertyName())) {
            final State newState = (State) evt.getNewValue();
            this.setBorder(borders.get(newState));
            this.repaint();
        }

    }
}