package comeon.core;

import com.google.inject.Inject;
import comeon.core.extmetadata.ExternalMetadataSource;
import comeon.model.Template;
import comeon.model.processors.PreProcessor;

import java.io.File;
import java.util.Set;

public final class DefaultMediaUploadBatchFactory implements MediaUploadBatchFactory {

    private final Set<PreProcessor> preProcessors;

    @Inject
    public DefaultMediaUploadBatchFactory(final Set<PreProcessor> preProcessors) {
        this.preProcessors = preProcessors;
    }

    @Override
    public MediaUploadBatch makeMediaUploadBatch(final File[] files, final Template defautTemplate, final ExternalMetadataSource<?> externalMetadataSource) {
        return new MediaUploadBatch(files, defautTemplate, preProcessors, externalMetadataSource);
    }

}
