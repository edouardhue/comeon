package comeon.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import comeon.Core;
import comeon.UserNotSetException;
import comeon.model.Picture;
import comeon.model.Template;
import comeon.model.TemplateKind;
import comeon.ui.pictures.PicturePanels;

public final class UI extends JFrame {
  @Deprecated
  public static final File TEMPLATE_FILE = new File("/home/sbdd8031/Projets/comeon/workspace/comeon/src/test/resources/simple.vm");

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
  
  private void add(final Picture picture) {
    final PicturePanels panels = new PicturePanels(picture);
    
    this.previews.remove(previewsGlue);
    this.previews.add(panels.getPreviewPanel());
    this.previews.add(previewsGlue);
    
    this.editContainer.add(panels.getEditPanel(), picture.getFileName());
    
    ((JComponent) panels.getPreviewPanel()).addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
          ((CardLayout) editContainer.getLayout()).show(editContainer, picture.getFileName());
        }
      }
    });

    this.invalidate();
  }
  
  public static void main(final String[] args) throws IOException, UserNotSetException {
    final Core core = Core.getInstance();
    final File[] files = new File[args.length];
    for (int i = 0; i < args.length; i++) {
      files[i] = new File(args[i]);
    }
    final String templateText = Files.toString(TEMPLATE_FILE, Charsets.UTF_8);
    core.addPictures(files, new Template("DEFAULT", "DEFAULT", TEMPLATE_FILE, templateText, TemplateKind.VELOCITY));
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        final UI ui = new UI();
        for (final Picture picture : core.getPictures()) {
          ui.add(picture);
        }
      }
    });
  }
}