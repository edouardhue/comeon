package comeon.model.processors;

import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.exif.GpsDirectory;

import java.util.Map;

public final class GpsPreProcessor implements PreProcessor {

    @Override
    public void process(final Directory directory, final Map<String, Object> metadata) {
        final GeoLocation geolocation = ((GpsDirectory) directory).getGeoLocation();
        metadata.put("geolocation", geolocation);
    }

    @Override
    public Class<? extends Directory> getSupportedClass() {
        return GpsDirectory.class;
    }

}
