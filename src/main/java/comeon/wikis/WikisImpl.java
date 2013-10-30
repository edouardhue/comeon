package comeon.wikis;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.ComeOn;
import comeon.core.WithPreferences;
import comeon.model.User;
import comeon.model.Wiki;

@Singleton
public final class WikisImpl implements Wikis, WithPreferences {

  private static final String DEFAULT_ACTIVE_WIKI_NAME = "Commons";

  private final ArrayList<Wiki> wikis;

  private final Preferences preferences;
  
  private final EventBus bus;
  
  private Wiki activeWiki;
  
  @Inject
  public WikisImpl(final EventBus bus) {
    this.bus = bus;
    this.wikis = new ArrayList<>(0);
    preferences = Preferences.userNodeForPackage(ComeOn.class).node("wikis");
  }
  
  @Override
  public void loadPreferences() throws BackingStoreException {
    final String activeWikiName = preferences.get(WikiPreferencesKeys.ACTIVE.name(), DEFAULT_ACTIVE_WIKI_NAME);
    final String[] wikiNames = preferences.childrenNames();
    this.wikis.ensureCapacity(wikiNames.length);
    for (final String name : wikiNames) {
      final Wiki wiki = this.readWiki(name);
      if (activeWikiName.equals(name)) {
        activeWiki = wiki;
      }
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
      node.put(WikiPreferencesKeys.URL.name(), wiki.getUrl());
      final Preferences userNode = node.node("user");
      userNode.put(UserPreferencesKeys.LOGIN.name(), wiki.getUser().getLogin());
      userNode.put(UserPreferencesKeys.PASSWORD.name(), wiki.getUser().getPassword());
      userNode.put(UserPreferencesKeys.DISPLAY_NAME.name(), wiki.getUser().getDisplayName());
    }
  }

  @Override
  public Wiki getActiveWiki() {
    return activeWiki;
  }
  
  @Override
  public void setActiveWiki(final Wiki wiki) {
    final ActiveWikiChangeEvent event = new ActiveWikiChangeEvent(this.activeWiki, wiki);
    this.activeWiki = wiki;
    preferences.put(WikiPreferencesKeys.ACTIVE.name(), wiki.getName());
    bus.post(event);
  }
  
  private Wiki readWiki(final String name) throws BackingStoreException {
    final Preferences node = preferences.node(name);
    final String url = node.get(WikiPreferencesKeys.URL.name(), null);
    final Wiki wiki = new Wiki(name, url, this.readUser(node));
    wikis.add(wiki);
    return wiki;
  }
  
  private User readUser(final Preferences wikiNode) throws BackingStoreException {
    final Preferences userNode = wikiNode.node("user");
    final String login = userNode.get(UserPreferencesKeys.LOGIN.name(), null);
    final String password = userNode.get(UserPreferencesKeys.PASSWORD.name(), null);
    final String displayName = userNode.get(UserPreferencesKeys.DISPLAY_NAME.name(), null);
    return new User(login, password, displayName);
  }
  
  private enum WikiPreferencesKeys {
    URL, ACTIVE
  }

  private enum UserPreferencesKeys {
    LOGIN, PASSWORD, DISPLAY_NAME
  }
}
