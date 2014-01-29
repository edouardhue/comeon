package comeon.core;

import java.io.File;

import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.model.Template;

public interface PicturesBatchFactory {
  PicturesBatch makePicturesBatch(File[] files, Template defautTemplate, ExternalMetadataSource<?> externalMetadataSource);
}
