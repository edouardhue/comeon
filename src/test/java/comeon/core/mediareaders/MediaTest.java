package comeon.core.mediareaders;

import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import comeon.core.MediaUploadBatch;
import comeon.core.extmetadata.NullMetadataSource;
import comeon.model.Media;
import comeon.model.Template;
import comeon.model.processors.GpsPreProcessor;
import comeon.model.processors.IptcPreProcessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * IPTC Keywords are limited to 64 bytes. Too bad !
 * 
 * @author Édouard Hue
 *
 */
public class MediaTest {
  private static final String SHORT_CAT_NAME = "Category:Women facing right and looking right";

  private static final String LONG_CAT_NAME_2 = "Category:Annual general meeting of Wikimédia France in October 2013";

  private static final String LONG_CAT_NAME_1 = "Category:21st-century black and white portrait photographs of women at bust length";

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();
  
  private PictureReader reader;

  private File file;
  
  @Before
  public void createReader() throws IOException {
    file = temp.newFile();
    Resources.asByteSource(Resources.getResource("long-category-titles.jpg")).copyTo(Files.asByteSink(file));
    this.reader = new PictureReader(file, null);
  }

  @Test
  public void testReadLongCategoryTitles() throws IOException, MediaReaderException {
    final Template mockTemplate = Mockito.mock(Template.class);
    Mockito.when(mockTemplate.getTemplateText()).thenReturn("");
    final MediaUploadBatch pics = new MediaUploadBatch(new File[0], mockTemplate, Sets.newHashSet(new IptcPreProcessor(), new GpsPreProcessor()), new NullMetadataSource());

    final Media pic = reader.buildMedia(pics);
    Assert.assertTrue(pic.getMetadata().containsKey("keywords"));
    final Set<String> keywords = Sets.newHashSet((String[]) pic.getMetadata().get("keywords"));
    Assert.assertFalse(keywords.contains(LONG_CAT_NAME_1));
    Assert.assertTrue(keywords.contains(new String(LONG_CAT_NAME_1.getBytes(Charsets.UTF_8), 0, 64, Charsets.UTF_8)));
    Assert.assertFalse(keywords.contains(LONG_CAT_NAME_2));
    Assert.assertTrue(keywords.contains(new String(LONG_CAT_NAME_2.getBytes(Charsets.UTF_8), 0, 64, Charsets.UTF_8)));
    Assert.assertTrue(keywords.contains(SHORT_CAT_NAME));
  }

}
