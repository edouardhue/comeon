package comeon.mediawiki;

import org.apache.http.impl.client.AbstractHttpClient;
import org.mediawiki.api.MWApi;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.model.Wiki;

@Singleton
public final class MediaWikiFactory {
  private final AbstractHttpClient httpClient;

  @Inject
  public MediaWikiFactory(AbstractHttpClient httpClient) {
    super();
    this.httpClient = httpClient;
  }
  
  public MediaWiki build(final Wiki wiki) {
    final MWApi api = new MWApi(wiki.getUrl(), httpClient);
    return new MediaWikiImpl(wiki, api);
  }
}
