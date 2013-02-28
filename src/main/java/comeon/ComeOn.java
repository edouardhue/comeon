package comeon;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import comeon.model.Picture;
import comeon.model.Template;
import comeon.model.TemplateKind;
import comeon.model.User;

public final class ComeOn {
  private static final Logger LOGGER = LoggerFactory.getLogger(ComeOn.class);

  private static final File TEMPLATE_FILE = new File("/home/sbdd8031/Projets/comeon/workspace/comeon/src/test/resources/simple.vm");

  public static void main(String[] args) throws IOException {
    final Core core = Core.getInstance();
    final File[] files = new File[args.length];
    for (int i = 0; i < args.length; i++) {
      files[i] = new File(args[i]);
    }
    final String templateText = Files.toString(TEMPLATE_FILE, Charsets.UTF_8);
    core.addPictures(files, new Template("DEFAULT", "DEFAULT", TEMPLATE_FILE, templateText, TemplateKind.VELOCITY));
    final JFrame frame = new JFrame("ComeOn!");
    final JDesktopPane dp = new JDesktopPane();
    dp.setPreferredSize(new Dimension(1024, 768));
    frame.getContentPane().add(dp);
    frame.setVisible(true);
    frame.pack();
    for (final Picture picture : core.getPictures()) {
      LOGGER.info("Showing image {}", picture.getFileName());
      System.out.println(picture.getRenderedTemplate());
      final ImageReader ir = ImageIO.getImageReadersByFormatName("jpeg").next();
      try (final ByteArrayInputStream bis = new ByteArrayInputStream(picture.getThumbnail())) {
        ir.setInput(ImageIO.createImageInputStream(bis));
        try {
          final BufferedImage im = ir.read(0);
          final JInternalFrame jf = new JInternalFrame(picture.getFileName(), false, false, false, false);
          LOGGER.info("{} {}", im.getWidth(), im.getHeight());
          jf.setVisible(true);
          dp.add(jf);
          jf.setSize(im.getWidth(), im.getHeight());
          jf.getContentPane().getGraphics().drawImage(im, 0, 0, null);
          jf.repaint();
        } catch (final IOException e) {
          LOGGER.warn("Can't display image {}", picture.getFileName(), e);
        }
      }
    }
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

}
