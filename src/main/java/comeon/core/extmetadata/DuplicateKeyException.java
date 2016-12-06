package comeon.core.extmetadata;

import comeon.ui.UI;

import java.text.MessageFormat;

public class DuplicateKeyException extends RuntimeException {
    private final Object dup;

    public DuplicateKeyException(final Object dup) {
        super(String.valueOf(dup));
        this.dup = dup;
    }

    public Object getDuplicate() {
        return dup;
    }

    @Override
    public String getLocalizedMessage() {
        return MessageFormat.format(UI.BUNDLE.getString("error.exception.duplicatekey"), String.valueOf(dup));
    }
}
