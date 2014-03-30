package comeon;

import java.awt.SplashScreen;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import javax.swing.SwingUtilities;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreProtocolPNames;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import comeon.core.Core;
import comeon.core.CoreImpl;
import comeon.core.PicturesBatchFactory;
import comeon.core.RealPicturesBatchFactory;
import comeon.core.WithPreferences;
import comeon.mediawiki.MediaWikiFactory;
import comeon.model.TemplateKind;
import comeon.model.processors.DefaultPostProcessor;
import comeon.model.processors.GpsPreProcessor;
import comeon.model.processors.IptcPreProcessor;
import comeon.model.processors.PostProcessor;
import comeon.model.processors.PreProcessor;
import comeon.model.processors.XmpPreProcessor;
import comeon.templates.Templates;
import comeon.templates.TemplatesImpl;
import comeon.templates.velocity.VelocityTemplate;
import comeon.ui.UI;
import comeon.ui.actions.AboutAction;
import comeon.ui.actions.AddPicturesAction;
import comeon.ui.actions.HelpManualAction;
import comeon.ui.actions.PreferencesAction;
import comeon.ui.actions.QuitAction;
import comeon.ui.actions.UploadPicturesAction;
import comeon.ui.menu.EditMenu;
import comeon.ui.menu.FileMenu;
import comeon.ui.menu.HelpMenu;
import comeon.ui.menu.MenuBar;
import comeon.ui.preferences.main.PreferencesController;
import comeon.ui.preferences.main.PreferencesDialog;
import comeon.ui.preferences.main.PreferencesModel;
import comeon.ui.preferences.main.PreferencesPanel;
import comeon.ui.preferences.main.TemplatesListPanel;
import comeon.ui.preferences.main.WikisListPanel;
import comeon.ui.preferences.templates.TemplateSubController;
import comeon.ui.preferences.templates.TemplateSubPanel;
import comeon.ui.preferences.wikis.WikiSubController;
import comeon.ui.preferences.wikis.WikiSubPanel;
import comeon.ui.toolbar.Toolbar;
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
  
  private DefaultHttpClient configureHttpClient() {
    final ClientConnectionManager connectionManager = new PoolingClientConnectionManager();
    final DefaultHttpClient client = new DefaultHttpClient(connectionManager);
    final String userAgentString = UI.BUNDLE.getString("useragent");
    client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, userAgentString);
    LOGGER.info("ComeOn! uses \"{}\" as User-Agent", userAgentString);
    return client;
  }
  
  @Override
  protected void configure() {
    bind(Core.class).to(CoreImpl.class);
    bind(Templates.class).to(TemplatesImpl.class);
    bind(Wikis.class).to(WikisImpl.class);
    bind(PicturesBatchFactory.class).to(RealPicturesBatchFactory.class);
    bind(MediaWikiFactory.class);
    
    Multibinder<PreProcessor> preProcessorsBinder = Multibinder.newSetBinder(binder(), PreProcessor.class);
    preProcessorsBinder.addBinding().to(GpsPreProcessor.class);
    preProcessorsBinder.addBinding().to(IptcPreProcessor.class);
    preProcessorsBinder.addBinding().to(XmpPreProcessor.class);
    
    Multibinder<PostProcessor> postProcessorsBinder = Multibinder.newSetBinder(binder(), PostProcessor.class);
    postProcessorsBinder.addBinding().to(DefaultPostProcessor.class);
    
    bind(ExecutorService.class).toInstance(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
    
    MapBinder<String, TemplateKind> templateKinds = MapBinder.newMapBinder(binder(), String.class, TemplateKind.class);
    templateKinds.addBinding(VelocityTemplate.class.getSimpleName()).to(VelocityTemplate.class);
    
    bind(AbstractHttpClient.class).toInstance(configureHttpClient());
    
    bind(UI.class);
    
    bind(PreferencesDialog.class);
    bind(PreferencesController.class);
    bind(PreferencesModel.class);
    bind(PreferencesPanel.class);
    bind(TemplatesListPanel.class);
    bind(WikisListPanel.class);
    
    bind(TemplateSubController.class);
    bind(TemplateSubPanel.class);
    
    bind(WikiSubController.class);
    bind(WikiSubPanel.class);
    
    bind(MenuBar.class);
    bind(FileMenu.class);
    bind(EditMenu.class);
    bind(HelpMenu.class);
    
    bind(Toolbar.class);
    
    bind(AddPicturesAction.class);
    bind(UploadPicturesAction.class);
    bind(PreferencesAction.class);
    bind(QuitAction.class);
    bind(HelpManualAction.class);
    bind(AboutAction.class);
    
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
      LOGGER.warn(UI.BUNDLE.getString("error.preferences.cantloaddefault.message"), e);
    }
  }
  
  public static void main(final String... args) throws Exception {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
    final Arguments arguments = new Arguments();
    final CmdLineParser parser = new CmdLineParser(arguments);
    try {
      parser.parseArgument(args);
      final ComeOn comeOn = new ComeOn();
      if (arguments.getRescue()) {
        comeOn.resetPreferences();
      } else {
        comeOn.checkPreferences();
      }
      final UI ui = assemble(comeOn);
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
    } catch (final CmdLineException e) {
      final StringWriter usageBuffer = new StringWriter();
      usageBuffer.append(UI.BUNDLE.getString("comeon"));
      usageBuffer.append(" – ");
      usageBuffer.append(UI.BUNDLE.getString("args.usage"));
      usageBuffer.append('\n');
      parser.printUsage(usageBuffer, UI.BUNDLE);
      System.err.println(usageBuffer.toString());
      usageBuffer.close();
    }
  }

  private static UI assemble(final ComeOn comeOn) throws BackingStoreException {
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
    comeOn.bus.register(injector.getInstance(AddPicturesAction.class));
    comeOn.bus.register(injector.getInstance(UploadPicturesAction.class));
    return ui;
  }

}
