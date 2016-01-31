package comeon.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.Core;
import comeon.model.Media;
import comeon.templates.Templates;
import comeon.ui.actions.MediaRemovedEvent;
import comeon.ui.actions.MediaAddedEvent;
import comeon.ui.add.AddModel;
import comeon.ui.add.AddMediaDialog;
import comeon.ui.media.MediaPanels;
import comeon.ui.menu.MenuBar;
import comeon.ui.toolbar.Toolbar;

@Singleton
public final class UI extends JFrame {
  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(UI.class);

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
    
    final MediaTransferHandler transferHandler = new MediaTransferHandler(templates);
    this.setTransferHandler(transferHandler);
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
  public void handleMediaAddedEvent(final MediaAddedEvent event) {
    this.refreshMedia();
  }

  @Subscribe
  public void handleMediaRemovedEvent(final MediaRemovedEvent event) {
    this.refreshMedia();
  }

  private void refreshMedia() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        previews.removeAll();
        editContainer.removeAll();
        validate();
        for (final Media media : core.getMedia()) {
          add(media);
        }
        validate();
      }
    });
  }

  private void add(final Media media) {
    final MediaPanels panels = new MediaPanels(media);
    final JComponent previewPanel = panels.getPreviewPanel();

    this.previews.remove(previewsGlue);
    this.previews.add(previewPanel);
    this.previews.add(previewsGlue);

    this.editContainer.add(panels.getEditPanel(), media.getFileName());

    previewPanel.addMouseListener(new PreviewPanelMouseAdapter(media));
  }

  private final class PreviewPanelMouseAdapter extends MouseAdapter {
    private final Media media;

    private PreviewPanelMouseAdapter(Media media) {
      this.media = media;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
      if (e.getButton() == MouseEvent.BUTTON1) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            if (e.isControlDown()) {
              core.removeMedia(media);
            } else {
              ((CardLayout) editContainer.getLayout()).show(editContainer, media.getFileName());
            }
          }
        });
      }
    }
  }
  
  private final class MediaTransferHandler extends TransferHandler {
    private static final long serialVersionUID = 1L;
    
    private final Templates templates;

    public MediaTransferHandler(final Templates templates) {
      this.templates = templates;
    }

    @Override
    public boolean canImport(final TransferSupport support) {
      return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }
    
    @Override
    public boolean importData(TransferSupport support) {

      try {
        @SuppressWarnings("unchecked")
        final List<File> files = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
        final List<File> filteredFiles = new ArrayList<File>(files.size());
        for (final File file : files) {
          if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")) {
            filteredFiles.add(file);
          }
        }
        final File[] preselectedFiles = filteredFiles.toArray(new File[filteredFiles.size()]);
        
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            final AddMediaDialog dialog = new AddMediaDialog(templates, preselectedFiles);
            final int value = dialog.showDialog();
            if (value == JOptionPane.OK_OPTION) {
              final AddModel model = dialog.getModel();
              final File[] files = model.getMediaFiles();
              if (files.length > 0) {
                core.addMedia(files, model.getTemplate(), model.getExternalMetadataSource());
              }
            }
          }
        });
        return true;
      } catch (final UnsupportedFlavorException | IOException e) {
        LOGGER.warn("Failed drag & drop transfer", e);
        return false;
      }
    }
  }

}