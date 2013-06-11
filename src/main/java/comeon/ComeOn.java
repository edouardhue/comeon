package comeon;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.BackingStoreException;

import javax.swing.SwingUtilities;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import comeon.commons.Commons;
import comeon.commons.CommonsImpl;
import comeon.core.Core;
import comeon.core.CoreImpl;
import comeon.templates.velocity.Templates;
import comeon.ui.UI;
import comeon.users.UserNotSetException;
import comeon.users.Users;

public final class ComeOn extends AbstractModule {

  public static void main(final String[] args) throws IOException, UserNotSetException, BackingStoreException {
    final Injector injector = Guice.createInjector(new ComeOn());
    final Templates templates = injector.getInstance(Templates.class);
    templates.readPreferences();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new UI(injector.getInstance(Core.class), injector.getInstance(Users.class), templates);
      }
    });
  }

  @Override
  protected void configure() {
    bind(Commons.class).to(CommonsImpl.class);
    bind(Core.class).to(CoreImpl.class);
    bind(ExecutorService.class).toInstance(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
  }

}
