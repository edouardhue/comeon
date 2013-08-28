package comeon;

import java.awt.SplashScreen;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import comeon.core.Core;
import comeon.core.CoreImpl;
import comeon.core.WithPreferences;
import comeon.mediawiki.MediaWiki;
import comeon.mediawiki.MediaWikiImpl;
import comeon.templates.Templates;
import comeon.templates.TemplatesImpl;
import comeon.ui.UI;
import comeon.ui.menu.EditMenu;
import comeon.ui.menu.FileMenu;
import comeon.ui.menu.HelpMenu;
import comeon.ui.menu.MenuBar;
import comeon.users.Users;
import comeon.users.UsersImpl;
import comeon.wikis.Wikis;
import comeon.wikis.WikisImpl;

public final class ComeOn extends AbstractModule {

  private final EventBus bus;

  public ComeOn() {
    this.bus = new EventBus();
  }

  @Override
  protected void configure() {
    bind(MediaWiki.class).to(MediaWikiImpl.class);
    bind(Core.class).to(CoreImpl.class);
    bind(Users.class).to(UsersImpl.class);
    bind(Templates.class).to(TemplatesImpl.class);
    bind(Wikis.class).to(WikisImpl.class);
    bind(ExecutorService.class).toInstance(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
    bind(UI.class);
    bind(MenuBar.class);
    bind(FileMenu.class);
    bind(EditMenu.class);
    bind(HelpMenu.class);
    bind(EventBus.class).toInstance(bus);
  }

  public static void main(final String[] args) throws Exception {
    final ComeOn comeOn = new ComeOn();
    final Injector injector = Guice.createInjector(comeOn);
    final List<WithPreferences<? extends Exception>> withPrefs = Arrays.asList(
        injector.getInstance(Templates.class),
        injector.getInstance(Users.class),
        injector.getInstance(Wikis.class)
    );
    for (final WithPreferences<? extends Exception> withPref : withPrefs) {
      withPref.loadPreferences();
    }
    final UI ui = injector.getInstance(UI.class);
    comeOn.bus.register(ui);
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null) {
          splash.close();
        }
        ui.setVisible(true);
      }
    });
  }

}
