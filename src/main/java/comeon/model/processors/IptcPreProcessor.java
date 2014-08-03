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
    final SimpleDateFormat inFormat = new SimpleDateFormat("HHmmssZ:yyyyMMdd", Locale.ENGLISH);
    final SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    try {
      final Date pictureDate = inFormat.parse(iptcTime + ":" + iptcDate);
      metadata.put(DATE, outFormat.format(pictureDate));
    } catch (final ParseException e) {
      LOGGER.warn("Can't handle date", e);
    }
  }

  @Override
  public Class<IptcDirectory> getSupportedClass() {
    return IptcDirectory.class;
  }
}
