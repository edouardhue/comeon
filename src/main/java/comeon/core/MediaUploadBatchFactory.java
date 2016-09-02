package comeon.core;

import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.model.Template;

import java.io.File;

public interface MediaUploadBatchFactory {
    MediaUploadBatch makeMediaUploadBatch(File[] files, Template defautTemplate, ExternalMetadataSource<?> externalMetadataSource);
}
