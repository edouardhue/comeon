package comeon.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.google.common.io.Resources;
import comeon.Core;
import comeon.model.Picture;
import comeon.ui.pictures.PicturePanels;

public final class UI extends JFrame {
  
  private static final long serialVersionUID = 1L;

  public static final ResourceBundle BUNDLE = ResourceBundle.getBundle("comeon.ui.comeon");
  
  public static final Color NEUTRAL_GREY = Color.DARK_GRAY;
  
  public static final int PREVIEW_PANEL_HEIGHT = 90;

  public static final int METADATA_PANEL_WIDTH = 280;

  private final Box previews;
  
  private final Component previewsGlue;
  
  private final JPanel editContainer;
  
  public UI() {
    super("ComeOn!");
    this.setIconImages(this.loadIcons());
    this.setJMenuBar(new MenuBar(this));
    this.setLayout(new BorderLayout());
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setMinimumSize(new Dimension(800, 600));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    this.previews = new Box(BoxLayout.X_AXIS);
    this.previews.setMinimumSize(new Dimension(0, PREVIEW_PANEL_HEIGHT));
    this.previews.setBackground(NEUTRAL_GREY);
    this.previews.setOpaque(true);
    this.previewsGlue = Box.createHorizontalGlue();
    this.previews.add(previewsGlue);
    final JScrollPane scrollablePreviews = new JScrollPane(previews, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    this.add(scrollablePreviews, BorderLayout.SOUTH);
    
    this.editContainer = new JPanel(new CardLayout());
    this.add(editContainer, BorderLayout.CENTER);
    
    this.setVisible(true);
  }

  private List<? extends Image> loadIcons() {
    try {
      return Arrays.asList(
          ImageIO.read(Resources.getResource("comeon_16_16.png")),
          ImageIO.read(Resources.getResource("comeon_48_48.png")),
          ImageIO.read(Resources.getResource("comeon_128_128.png"))
      );
    } catch (final IOException e) {
      return Collections.emptyList();
    }
  }
  
  public void refreshPictures() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        previews.removeAll();
        editContainer.removeAll();
        for (final Picture picture : Core.getInstance().getPictures()) {
          add(picture);
        }
      }
    });
  }
  
  public void add(final Picture picture) {
    final PicturePanels panels = new PicturePanels(picture);
    
    this.previews.remove(previewsGlue);
    this.previews.add(panels.getPreviewPanel());
    this.previews.add(previewsGlue);
    
    this.editContainer.add(panels.getEditPanel(), picture.getFileName());
    
    ((JComponent) panels.getPreviewPanel()).addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              ((CardLayout) editContainer.getLayout()).show(editContainer, picture.getFileName());
            }
          });
        }
      }
    });
    
    this.validate();
  }
}