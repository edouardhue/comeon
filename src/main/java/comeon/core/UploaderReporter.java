package comeon.core;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Singleton;
import comeon.core.events.UploadDoneEvent;
import comeon.model.Media;
import comeon.ui.actions.MediaAddedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Singleton
public class UploaderReporter {

    private static final Logger LOGGER = LoggerFactory.getLogger("UPLOAD_REPORTER");

    @Subscribe
    public void logMediaAddition(final MediaAddedEvent event) {
        LOGGER.info("MEDIA ADDED\n\tTEMPLATE {}\n\tEXT. METADATA {}", event.getBatch().getTemplate(), event.getBatch().getExternalMetadataSource());
        event.getBatch().getMedia().forEach(this::log);
    }

    private void log(final Media media) {
        LOGGER.info("MEDIA ADDED\n\tFILE {}", media.getFile());
    }

    @Subscribe
    public void logUpload(final UploadDoneEvent event) {
        event.getReports().forEach(this::log);
    }

    private void log(final UploadReport report) {
        LOGGER.info("UPLOAD COMPLETED\n\tFILE {}\n\tSTATE {}", report.getMedia().getFile(), report.getMedia().getState(), report.getCause());
    }

    public Optional<String> findLoggingFileLocation() {
        final Optional<String> fileLocation;
        if (LOGGER instanceof ch.qos.logback.classic.Logger) {
            final ch.qos.logback.classic.Logger logbackLogger = (ch.qos.logback.classic.Logger) LOGGER;
            final Appender<ILoggingEvent> appender = logbackLogger.getAppender("UPLOAD_REPORTER");
            if (appender != null && appender instanceof FileAppender) {
                final FileAppender fileAppender = (FileAppender) appender;
                fileLocation = Optional.of(fileAppender.getFile());
            } else {
                fileLocation = Optional.empty();
            }
        } else {
            fileLocation = Optional.empty();
        }
        return fileLocation;
    }
}
