package comeon.core.extmetadata;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class KeyTransformer {
    private final Pattern regex;

    private static final Pattern SUBSTITUTION_PATTERN = Pattern.compile("\\$\\{(\\d+)\\}");

    private final String substitution;

    public KeyTransformer(final String regexp, final String substitution) {
        this.regex = Pattern.compile(regexp);
        this.substitution = substitution;
    }

    public String transform(final String input) {
        final Matcher substitutionMatcher = SUBSTITUTION_PATTERN.matcher(substitution);
        final Matcher m = regex.matcher(input);
        final String transformed;
        if (m.matches()) {
            final StringBuffer transforming = new StringBuffer();
            while (substitutionMatcher.find()) {
                final int groupReference = Integer.valueOf(substitutionMatcher.group(1));
                final String groupValue = m.group(groupReference);
                substitutionMatcher.appendReplacement(transforming, groupValue);
            }
            substitutionMatcher.appendTail(transforming);
            transformed = transforming.toString();
        } else {
            transformed = input;
        }
        return transformed;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("regex", regex)
                .append("substitution", substitution)
                .toString();
    }
}
