package comeon.ui.preferences;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.mockito.Mockito;

import com.google.common.base.Charsets;
import comeon.model.Template;
import comeon.model.TemplateKind;
import comeon.model.User;
import comeon.model.Wiki;
import comeon.model.processors.PostProcessor;
import comeon.templates.Templates;
import comeon.templates.velocity.VelocityTemplate;
import comeon.wikis.Wikis;

public final class PreferencesDialogTest {
  
  public static void main(final String... args) {
    final Wikis wikis = Mockito.mock(Wikis.class);
    final VelocityTemplate kind = new VelocityTemplate(new HashSet<PostProcessor>());
    final Templates templates = Mockito.mock(Templates.class);
    Mockito.when(templates.getTemplates()).thenReturn(Arrays.asList(
        new Template("name1", "description1", new File("."), Charsets.UTF_8, "text1", kind),
        new Template("name2", "description2", new File("."), Charsets.UTF_8, "text2", kind),
        new Template("name3", "description3", new File("."), Charsets.UTF_8, "text3", kind),
        new Template("name4", "description4", new File("."), Charsets.UTF_8, "text4", kind)
        ));
    Mockito.when(wikis.getWikis()).thenReturn(Arrays.asList(
        new Wiki("wiki1", "http://wiki1/", new User("user1", "password1", "Display Name 1")),
        new Wiki("wiki2", "http://wiki2/", new User("user2", "password1", "Display Name 2")),
        new Wiki("wiki3", "http://wiki3/", new User("user3", "password1", "Display Name 3")),
        new Wiki("wiki4", "http://wiki4/", new User("user4", "password1", "Display Name 4"))
        ));
    final PreferencesController controller = new PreferencesController(templates, wikis);
    
    SwingUtilities.invokeLater(new Runnable() {
      
      @Override
      public void run() {
        final JFrame f = new JFrame();
        final PreferencesModel model = new PreferencesModel();
        final PreferencesPanel view = new PreferencesPanel(
          new Charset[] {Charsets.ISO_8859_1, Charsets.UTF_8},
          new TemplateKind[] { kind }
        );

        controller.registerModel(model);
        controller.registerView(view);
        
        f.add(view);
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
      }
    });
  }
}
