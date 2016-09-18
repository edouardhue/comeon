package comeon.core.events;

import comeon.core.UploadReport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class UploadDoneEvent {

    private final List<UploadReport> reports;

    private final String reportFileLocation;

    public UploadDoneEvent(final List<UploadReport> reports, final String reporFileLocation) {
        this.reports = new ArrayList<>(reports);
        this.reportFileLocation = reporFileLocation;
    }

    public List<UploadReport> getReports() {
        return Collections.unmodifiableList(reports);
    }

    public Optional<String> getReportFileLocation() {
        return Optional.ofNullable(reportFileLocation);
    }
}
