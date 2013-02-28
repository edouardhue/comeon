package comeon.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import comeon.Core;
import comeon.model.Picture;
import comeon.model.Template;
import comeon.model.TemplateKind;

public final class UI extends JFrame {
  private static final File TEMPLATE_FILE = new File("/home/sbdd8031/Projets/comeon/workspace/comeon/src/test/resources/simple.vm");

  private static final long serialVersionUID = 1L;

  public static final ResourceBundle BUNDLE = ResourceBundle.getBundle("comeon.ui.comeon");
  
  public static final Color NEUTRAL_GREY = Color.DARK_GRAY;
  
  public static final int PREVIEW_PANEL_HEIGHT = 90;

  private final Box previews;
  
  private final Component previewsGlue;
  
  public UI() {
    this.setJMenuBar(new MenuBar());
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
    this.setVisible(true);
  }
  
  public void add(final Picture picture) {
    final PicturePanels panels = new PicturePanels(picture);
    this.previews.remove(previewsGlue);
    this.previews.add(panels.getPreviewPanel());
    this.previews.add(previewsGlue);
    this.previews.invalidate();
  }
  
  public static void main(final String[] args) throws IOException {
    final Core core = Core.getInstance();
    final File[] files = new File[args.length];
    for (int i = 0; i < args.length; i++) {
      files[i] = new File(args[i]);
    }
    final String templateText = Files.toString(TEMPLATE_FILE, Charsets.UTF_8);
    core.addPictures(files, new Template("DEFAULT", "DEFAULT", TEMPLATE_FILE, templateText, TemplateKind.VELOCITY));
    final UI ui = new UI();
    for (final Picture picture : core.getPictures()) {
      ui.add(picture);
    }
  }
}