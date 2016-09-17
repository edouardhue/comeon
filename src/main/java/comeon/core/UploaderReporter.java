package comeon.core;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Singleton;
import comeon.core.events.UploadDoneEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class UploaderReporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploaderReporter.class);

    @Subscribe
    public void logUpload(final UploadDoneEvent event) {
        event.getReports().forEach(this::log);
    }

    private void log(final UploadReport report) {
        LOGGER.info("{}: {}", report.getMedia().getFile(), report.getMedia().getState(), report.getCause());
    }
}
