package comeon.model.processors;

import com.drew.metadata.Directory;
import com.drew.metadata.iptc.IptcDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class IptcPreProcessor implements PreProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(IptcPreProcessor.class);

    public static final String DATE = "date";

    public static final String KEYWORDS = "keywords";

    private static final DateTimeFormatter IPTC_DATE_TIME_PARSER = DateTimeFormatter.ofPattern("[uuuuMMdd]:[HHmmss[Z]]", Locale.ENGLISH);

    private static final DateTimeFormatter METADATA_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("[uuuu-MM-dd] [HH:mm:ss]", Locale.ENGLISH);

    @Override
    public void process(final Directory directory, final Map<String, Object> metadata) {
        final String[] keywords = directory.getStringArray(IptcDirectory.TAG_KEYWORDS);
        metadata.put(KEYWORDS, keywords);

        final Optional<String> iptcDate = Optional.ofNullable(directory.getString(IptcDirectory.TAG_DIGITAL_DATE_CREATED));
        final Optional<String> iptcTime = Optional.ofNullable(directory.getString(IptcDirectory.TAG_DIGITAL_TIME_CREATED));

        final StringBuilder buf = new StringBuilder();
        iptcDate.ifPresent(buf::append);
        buf.append(':');
        iptcTime.ifPresent(buf::append);

        try {
            final TemporalAccessor iptcDateTime = IPTC_DATE_TIME_PARSER.parse(buf);
            metadata.put(DATE, METADATA_DATE_TIME_FORMATTER.format(iptcDateTime).trim());
        } catch (final DateTimeException e) {
            LOGGER.info("Can't handle date", e);
        }
    }

    @Override
    public Class<IptcDirectory> getSupportedClass() {
        return IptcDirectory.class;
    }
}
