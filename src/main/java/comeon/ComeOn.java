package comeon;

import java.awt.SplashScreen;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import comeon.core.Core;
import comeon.core.CoreImpl;
import comeon.core.WithPreferences;
import comeon.templates.Templates;
import comeon.templates.TemplatesImpl;
import comeon.ui.UI;
import comeon.ui.menu.EditMenu;
import comeon.ui.menu.FileMenu;
import comeon.ui.menu.HelpMenu;
import comeon.ui.menu.MenuBar;
import comeon.wikis.Wikis;
import comeon.wikis.WikisImpl;

public final class ComeOn extends AbstractModule {

  private static final Logger LOGGER = LoggerFactory.getLogger(ComeOn.class);
  
  private final EventBus bus;

  private final Preferences preferences;

  public ComeOn() {
    this.bus = new EventBus();
    this.preferences = Preferences.userNodeForPackage(ComeOn.class);
  }
  
  @Override
  protected void configure() {
    bind(Core.class).to(CoreImpl.class);
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
  
  private void resetPreferences() throws BackingStoreException {
    this.preferences.removeNode();
    this.loadDefaults();
  }

  private void checkPreferences() throws BackingStoreException {
    if (this.preferences.childrenNames().length == 0) {
      loadDefaults();
    }
  }

  private void loadDefaults() {
    try {
      Preferences.importPreferences(WikisImpl.class.getResourceAsStream("defaultPreferences.xml"));
    } catch (final InvalidPreferencesFormatException | IOException e) {
      // TODO i18n
      LOGGER.warn("Can't load default preferences", e);
    }
  }
  
  public static void main(final String[] args) throws Exception {
    final ComeOn comeOn = new ComeOn();
    // TODO Improve parameters handling
    if (args.length == 1 && "--rescue".equals(args[0])) {
      comeOn.resetPreferences();
    } else {
      comeOn.checkPreferences();
    }
    final Injector injector = Guice.createInjector(comeOn);
    final List<WithPreferences> withPrefs = Arrays.asList(
        injector.getInstance(Templates.class),
        injector.getInstance(Wikis.class)
    );
    for (final WithPreferences withPref : withPrefs) {
      withPref.loadPreferences();
    }
    final UI ui = injector.getInstance(UI.class);
    comeOn.bus.register(ui);
    comeOn.bus.register(injector.getInstance(Core.class));
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
