package comeon;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.BackingStoreException;

import javax.swing.SwingUtilities;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import comeon.core.Core;
import comeon.core.CoreImpl;
import comeon.mediawiki.MediaWiki;
import comeon.mediawiki.MediaWikiImpl;
import comeon.templates.Templates;
import comeon.templates.TemplatesImpl;
import comeon.ui.UI;
import comeon.ui.menu.EditMenu;
import comeon.ui.menu.FileMenu;
import comeon.ui.menu.HelpMenu;
import comeon.ui.menu.MenuBar;
import comeon.users.UserNotSetException;
import comeon.users.Users;
import comeon.users.UsersImpl;

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
    bind(ExecutorService.class).toInstance(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
    bind(UI.class);
    bind(MenuBar.class);
    bind(FileMenu.class);
    bind(EditMenu.class);
    bind(HelpMenu.class);
    bind(EventBus.class).toInstance(bus);
  }

  public static void main(final String[] args) throws IOException, UserNotSetException, BackingStoreException {
    final ComeOn comeOn = new ComeOn();
    final Injector injector = Guice.createInjector(comeOn);
    final Templates templates = injector.getInstance(Templates.class);
    templates.readPreferences();
    final UI ui = injector.getInstance(UI.class);
    comeOn.bus.register(ui);
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        ui.setVisible(true);
      }
    });
  }

}
