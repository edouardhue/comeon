package comeon.core;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import com.drew.imaging.ImageProcessingException;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import comeon.model.Picture;
import comeon.model.Template;

/**
 * IPTC Keywords are limited to 64 bytes. Too bad !
 * 
 * @author Édouard Hue
 *
 */
public class PicturesTest {
  private static final String SHORT_CAT_NAME = "Category:Women facing right and looking right";

  private static final String LONG_CAT_NAME_2 = "Category:Annual general meeting of Wikimédia France in October 2013";

  private static final String LONG_CAT_NAME_1 = "Category:21st-century black and white portrait photographs of women at bust length";

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();
  
  private PicturesBatch.PictureReader reader;
  
  @Before
  public void createReader() throws IOException {
    final File file = temp.newFile();
    Files.copy(Resources.newInputStreamSupplier(Resources.getResource("long-category-titles.jpg")), file);
    final Template mockTemplate = Mockito.mock(Template.class);
    Mockito.when(mockTemplate.getTemplateText()).thenReturn("");
    final PicturesBatch pics = new PicturesBatch(new File[0], mockTemplate, null);
    this.reader = pics.new PictureReader(file, null);
  }

  @Test
  public void testReadLongCategoryTitles() throws ImageProcessingException, IOException {
    final Picture pic = reader.buildPicture();
    Assert.assertTrue(pic.getMetadata().containsKey("keywords"));
    final Set<String> keywords = Sets.newHashSet((String[]) pic.getMetadata().get("keywords"));
    Assert.assertFalse(keywords.contains(LONG_CAT_NAME_1));
    Assert.assertTrue(keywords.contains(new String(LONG_CAT_NAME_1.getBytes(Charsets.UTF_8), 0, 64, Charsets.UTF_8)));
    Assert.assertFalse(keywords.contains(LONG_CAT_NAME_2));
    Assert.assertTrue(keywords.contains(new String(LONG_CAT_NAME_2.getBytes(Charsets.UTF_8), 0, 64, Charsets.UTF_8)));
    Assert.assertTrue(keywords.contains(SHORT_CAT_NAME));
  }

}
