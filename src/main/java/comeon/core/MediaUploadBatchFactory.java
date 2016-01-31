package comeon.core;

import java.io.File;

import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.model.Template;

public interface MediaUploadBatchFactory {
  MediaUploadBatch makeMediaUploadBatch(File[] files, Template defautTemplate, ExternalMetadataSource<?> externalMetadataSource);
}
