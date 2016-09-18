package comeon.core.extmetadata;

import comeon.model.Media;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Map;

public final class NullMetadataSource implements ExternalMetadataSource<Void> {
    @Override
    public void loadMetadata() {
    }

    @Override
    public Void getMediaMetadata(final Media media, final Map<String, Object> mediaMetadata) {
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
