package comeon.model.processors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.metadata.Directory;
import com.drew.metadata.iptc.IptcDirectory;

public final class IptcPreProcessor implements PreProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(IptcPreProcessor.class);

  private static final String DATE = "date";

  private static final String KEYWORDS = "keywords";

  @Override
  public void process(final Directory directory, final Map<String, Object> metadata) {
    final String[] keywords = directory.getStringArray(IptcDirectory.TAG_KEYWORDS);
    metadata.put(KEYWORDS, keywords);
    final String iptcDate = directory.getString(IptcDirectory.TAG_DIGITAL_DATE_CREATED);
    final String iptcTime = directory.getString(IptcDirectory.TAG_DIGITAL_TIME_CREATED);
    final SimpleDateFormat inFormatWithoutTimezone = new SimpleDateFormat("HHmmss:yyyyMMdd", Locale.ENGLISH);
    final SimpleDateFormat inFormatWithTimezone = new SimpleDateFormat("HHmmssZ:yyyyMMdd", Locale.ENGLISH);
    final SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    try {
      final Date pictureDateWithoutTimezone = inFormatWithoutTimezone.parse(iptcTime + ":" + iptcDate);
      metadata.put(DATE, outFormat.format(pictureDateWithoutTimezone));
    } catch (final ParseException e) {
      try {
        final Date pictureDateWithTimezone = inFormatWithTimezone.parse(iptcTime + ":" + iptcDate);
        metadata.put(DATE, outFormat.format(pictureDateWithTimezone));
      } catch (final ParseException e2) {
        LOGGER.info("Can't handle date", e2);
      }
    }
  }

  @Override
  public Class<IptcDirectory> getSupportedClass() {
    return IptcDirectory.class;
  }
}
