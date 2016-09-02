package comeon.wikis;

import comeon.model.Wiki;

public final class ActiveWikiChangeEvent {
    private final Wiki previouslyActiveWiki;

    private final Wiki newActiveWiki;

    public ActiveWikiChangeEvent(final Wiki previouslyActiveWiki, final Wiki newActiveWiki) {
        this.previouslyActiveWiki = previouslyActiveWiki;
        this.newActiveWiki = newActiveWiki;
    }

    public Wiki getPreviouslyActiveWiki() {
        return previouslyActiveWiki;
    }

    public Wiki getNewActiveWiki() {
        return newActiveWiki;
    }
}
