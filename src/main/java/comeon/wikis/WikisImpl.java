package comeon.wikis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ComeOn;
import comeon.core.WithPreferences;
import comeon.model.Wiki;

@Singleton
public final class WikisImpl implements Wikis, WithPreferences<BackingStoreException> {
  private static final Logger LOGGER = LoggerFactory.getLogger(WikisImpl.class);

  private final ArrayList<Wiki> wikis;

  private final Preferences preferences;
  
  @Inject
  public WikisImpl() {
    this.wikis = new ArrayList<>(0);
    preferences = Preferences.userNodeForPackage(ComeOn.class).node("wikis");
  }
  
  @Override
  public void loadPreferences() throws BackingStoreException {
    if (preferences.childrenNames().length == 0) {
      this.loadDefaults();
    }
    final String[] wikiNames = preferences.childrenNames();
    this.wikis.ensureCapacity(wikiNames.length);
    for (final String name : wikiNames) {
      this.readNode(name);
    }
  }
  
  @Override
  public List<Wiki> getWikis() {
    return new ArrayList<>(wikis);
  }
  
  @Override
  public void setWikis(final List<Wiki> wikis) {
    this.wikis.clear();
    this.wikis.ensureCapacity(wikis.size());
    this.wikis.addAll(wikis);
  }
  
  @Override
  public void save() throws BackingStoreException {
    for (final String name : preferences.childrenNames()) {
      preferences.node(name).removeNode();
    }
    for (final Wiki wiki : wikis) {
      final Preferences node = preferences.node(wiki.getName());
      node.put(PreferencesKeys.URL.name(), wiki.getUrl());
    }
  }
  
  private void readNode(final String name) throws BackingStoreException {
    final Preferences node = preferences.node(name);
    final String url = node.get(PreferencesKeys.URL.name(), null);
    final Wiki wiki = new Wiki(name, url);
    wikis.add(wiki);
  }
  
  private void loadDefaults() {
    try {
      Preferences.importPreferences(WikisImpl.class.getResourceAsStream("defaultPreferences.xml"));
    } catch (final InvalidPreferencesFormatException | IOException e) {
      // TODO i18n
      LOGGER.warn("Can't load default preferences", e);
    }
  }
  
  private enum PreferencesKeys {
    URL
  }
}
