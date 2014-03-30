package comeon.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
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
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.Core;
import comeon.model.Picture;
import comeon.templates.Templates;
import comeon.ui.actions.PictureRemovedEvent;
import comeon.ui.actions.PicturesAddedEvent;
import comeon.ui.menu.MenuBar;
import comeon.ui.pictures.PicturePanels;
import comeon.ui.toolbar.Toolbar;

@Singleton
public final class UI extends JFrame {
  private static final long serialVersionUID = 1L;

  public static final ResourceBundle BUNDLE = ResourceBundle.getBundle("comeon.ui.comeon");

  public static final Color NEUTRAL_GREY = Color.DARK_GRAY;

  public static final int PREVIEW_PANEL_HEIGHT = 90;

  public static final int METADATA_PANEL_WIDTH = 280;

  public static final List<? extends Image> ICON_IMAGES = loadIcons();

  private final Box previews;

  private final Component previewsGlue;

  private final JPanel editContainer;

  private final Core core;

  @Inject
  public UI(final Core core, final Templates templates, final MenuBar menuBar, final Toolbar toolbar) {
    super(BUNDLE.getString("comeon"));

    this.core = core;

    this.setIconImages(ICON_IMAGES);
    this.setJMenuBar(menuBar);
    this.setLayout(new BorderLayout());
    this.setExtendedState(Frame.MAXIMIZED_BOTH);
    this.setMinimumSize(new Dimension(800, 600));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.previews = new Box(BoxLayout.X_AXIS);
    this.previews.setMinimumSize(new Dimension(0, PREVIEW_PANEL_HEIGHT));
    this.previews.setBackground(NEUTRAL_GREY);
    this.previews.setOpaque(true);
    this.previewsGlue = Box.createHorizontalGlue();
    this.previews.add(previewsGlue);
    final JScrollPane scrollablePreviews = new JScrollPane(previews, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    this.add(scrollablePreviews, BorderLayout.SOUTH);

    this.editContainer = new JPanel(new CardLayout());
    this.add(editContainer, BorderLayout.CENTER);
    
    this.add(toolbar, BorderLayout.NORTH);
  }

  private static List<? extends Image> loadIcons() {
    try {
      return ImmutableList.of(ImageIO.read(Resources.getResource("comeon_16_16.png")),
          ImageIO.read(Resources.getResource("comeon_48_48.png")),
          ImageIO.read(Resources.getResource("comeon_128_128.png")));
    } catch (final IOException e) {
      return Collections.emptyList();
    }
  }

  @Subscribe
  public void handlePicturesAddedEvent(final PicturesAddedEvent event) {
    this.refreshPictures();
  }

  @Subscribe
  public void handlePictureRemovedEvent(final PictureRemovedEvent event) {
    this.refreshPictures();
  }

  private void refreshPictures() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        previews.removeAll();
        editContainer.removeAll();
        validate();
        for (final Picture picture : core.getPictures()) {
          add(picture);
        }
        validate();
      }
    });
  }

  private void add(final Picture picture) {
    final PicturePanels panels = new PicturePanels(picture);
    final JComponent previewPanel = panels.getPreviewPanel();

    this.previews.remove(previewsGlue);
    this.previews.add(previewPanel);
    this.previews.add(previewsGlue);

    this.editContainer.add(panels.getEditPanel(), picture.getFileName());

    previewPanel.addMouseListener(new PreviewPanelMouseAdapter(picture));
  }

  private final class PreviewPanelMouseAdapter extends MouseAdapter {
    private final Picture picture;

    private PreviewPanelMouseAdapter(Picture picture) {
      this.picture = picture;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
      if (e.getButton() == MouseEvent.BUTTON1) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            if (e.isControlDown()) {
              core.removePicture(picture);
            } else {
              ((CardLayout) editContainer.getLayout()).show(editContainer, picture.getFileName());
            }
          }
        });
      }
    }
  }

}