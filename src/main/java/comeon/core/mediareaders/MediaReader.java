package comeon.core.mediareaders;

import comeon.core.MediaUploadBatch;
import comeon.model.Media;

import java.util.Optional;

public interface MediaReader {
    Optional<Media> readMedia(MediaUploadBatch context);
}
