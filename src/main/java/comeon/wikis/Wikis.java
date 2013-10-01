package comeon.wikis;

import java.util.List;
import java.util.prefs.BackingStoreException;

import comeon.core.WithPreferences;
import comeon.model.Wiki;

public interface Wikis extends WithPreferences {
  List<Wiki> getWikis();
  void setWikis(List<Wiki> wikis);
  void save() throws BackingStoreException;
  Wiki getActiveWiki();
  void setActiveWiki(Wiki wiki);
}
