package comeon.ui.actions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import comeon.core.UploaderReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Singleton
public class BrowseToReportAction extends BaseAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrowseToReportAction.class);

    private final UploaderReporter reporter;

    @Inject
    public BrowseToReportAction(final UploaderReporter reporter) {
        super("report");
        this.reporter = reporter;
        this.setEnabled(reporter.findLoggingFileLocation().isPresent());
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        reporter.findLoggingFileLocation().ifPresent(location -> {
            if (Desktop.isDesktopSupported()) {
                final Desktop desktop = Desktop.getDesktop();
                final Path report = Paths.get(location);
                try {
                    desktop.open(report.getParent().toFile());
                } catch (final IOException ex) {
                    LOGGER.warn("Could not open report directory", e);
                }
            }
        });
    }
}
