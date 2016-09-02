package comeon.model.processors;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.impl.XMPNodeUtils;
import com.drew.metadata.Directory;
import com.drew.metadata.xmp.XmpDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public final class XmpPreProcessor implements PreProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmpPreProcessor.class);

    private static final String XMP_DOCUMENT_ID = "xmpDocumentID";

    @Override
    public void process(final Directory directory, final Map<String, Object> metadata) {
        final XmpDirectory xmpDir = (XmpDirectory) directory;
        final XMPMeta xmp = xmpDir.getXMPMeta();
        try {
            final String documentId = xmp.getPropertyString(XMPNodeUtils.NS_XMP_MM, "DocumentID");
            metadata.put(XMP_DOCUMENT_ID, documentId);
        } catch (final XMPException e) {
            LOGGER.warn("Could not read xmpDocumentID", e);
        }

    }

    @Override
    public Class<? extends Directory> getSupportedClass() {
        return XmpDirectory.class;
    }

}
