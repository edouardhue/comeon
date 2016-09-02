package comeon.core;

import in.yuvi.http.fluent.ProgressListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public final class ProgressListenerAdapter implements ProgressListener {

    public static final String TRANSFERRED = "transferred";

    public static final String TOTAL = "total";

    private final PropertyChangeSupport pcs;

    private Long transferred;

    private Long total;

    public ProgressListenerAdapter() {
        this.pcs = new PropertyChangeSupport(this);
        transferred = null;
        total = null;
    }

    @Override
    public void onProgress(final long transferred, final long total) {
        final Long oldTransferred = this.transferred;
        this.transferred = transferred;
        pcs.firePropertyChange(TRANSFERRED, oldTransferred, transferred);

        final Long oldTotal = this.total;
        this.total = total;
        pcs.firePropertyChange(TOTAL, oldTotal, total);
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

}
