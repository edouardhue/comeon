package comeon.core.events;

import comeon.core.UploadReport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class UploadDoneEvent {

    private final List<UploadReport> reports;

    public UploadDoneEvent(final List<UploadReport> reports) {
        this.reports = new ArrayList<>(reports);
    }

    public List<UploadReport> getReports() {
        return Collections.unmodifiableList(reports);
    }
}
