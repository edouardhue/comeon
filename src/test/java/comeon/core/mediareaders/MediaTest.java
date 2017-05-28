package comeon.core.mediareaders;

import com.drew.lang.GeoLocation;
import com.drew.metadata.exif.ExifSubIFDDescriptor;
import com.drew.metadata.exif.ExifSubIFDDirectory;
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
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

/**
 * IPTC Keywords are limited to 64 bytes. Too bad !
 *
 * @author Édouard Hue
 */
public class MediaTest {
    private static final String SHORT_CAT_NAME = "Category:Women facing right and looking right";

    private static final String LONG_CAT_NAME_2 = "Category:Annual general meeting of Wikimédia France in October 2013";

    private static final String LONG_CAT_NAME_1 = "Category:21st-century black and white portrait photographs of women at bust length";

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private Media longCategoryTitles, sample;

    @Before
    public void createReader() throws IOException, MediaReaderException {
        final Template mockTemplate = Mockito.mock(Template.class);
        Mockito.when(mockTemplate.getTemplateText()).thenReturn("");
        final MediaUploadBatch batch = new MediaUploadBatch(new File[0], mockTemplate, Sets.newHashSet(new IptcPreProcessor(), new GpsPreProcessor()), new NullMetadataSource());
        this.longCategoryTitles = read("long-category-titles.jpg", batch);
        this.sample = read("sample.jpg", batch);
    }

    private Media read(final String resource, final MediaUploadBatch batch) throws IOException, MediaReaderException {
        final File file = temp.newFile();
        Resources.asByteSource(Resources.getResource(resource)).copyTo(Files.asByteSink(file));
        final PictureReader reader = new PictureReader(file, null);
        return reader.buildMedia(batch);
    }

    @Test
    public void testReadLongCategoryTitles() {
        Assert.assertTrue(longCategoryTitles.getMetadata().containsKey(IptcPreProcessor.KEYWORDS));
        final Set<String> keywords = Sets.newHashSet((String[]) longCategoryTitles.getMetadata().get(IptcPreProcessor.KEYWORDS));
        Assert.assertFalse(keywords.contains(LONG_CAT_NAME_1));
        Assert.assertTrue(keywords.contains(new String(LONG_CAT_NAME_1.getBytes(Charsets.UTF_8), 0, 64, Charsets.UTF_8)));
        Assert.assertFalse(keywords.contains(LONG_CAT_NAME_2));
        Assert.assertTrue(keywords.contains(new String(LONG_CAT_NAME_2.getBytes(Charsets.UTF_8), 0, 64, Charsets.UTF_8)));
        Assert.assertTrue(keywords.contains(SHORT_CAT_NAME));
    }

    @Test
    public void testSample() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final Map<String, Object> metadata = sample.getMetadata();
        Assert.assertNotNull(metadata.get(IptcPreProcessor.KEYWORDS));
        Assert.assertEquals("2016-09-24 16:11:00", metadata.get(IptcPreProcessor.DATE));
        Assert.assertEquals(new GeoLocation(48d, 2d), metadata.get(GpsPreProcessor.GEOLOCATION));

        Assert.assertEquals("Commentaire utilisateur", PropertyUtils.getProperty(metadata.get("ExifSubIFD"), "UserComment"));
        Assert.assertEquals("20mm", PropertyUtils.getProperty(metadata.get("ExifSubIFD"), "LensModel"));
        Assert.assertEquals("0000000000", PropertyUtils.getProperty(metadata.get("ExifSubIFD"), "LensSerialNumber"));
        Assert.assertEquals("Description", PropertyUtils.getProperty(metadata.get("ExifIFD0"), "ImageDescription"));

        Assert.assertEquals("Canon", PropertyUtils.getProperty(metadata.get("ExifIFD0"), "Make"));
        Assert.assertEquals("Canon EOS 60D", PropertyUtils.getProperty(metadata.get("ExifIFD0"), "Model"));
        Assert.assertEquals("Édouard Hue", PropertyUtils.getProperty(metadata.get("ExifIFD0"), "Artist"));
        Assert.assertEquals("Édouard Hue", PropertyUtils.getProperty(metadata.get("ExifIFD0"), "Copyright"));

        Assert.assertEquals("Titre", PropertyUtils.getProperty(metadata.get("IPTC"), "ObjectName"));
        Assert.assertEquals("Édouard Hue", PropertyUtils.getProperty(metadata.get("IPTC"), "Byline"));
        Assert.assertEquals("Q1", PropertyUtils.getProperty(metadata.get("IPTC"), "Sublocation"));
        Assert.assertEquals("Édouard Hue", PropertyUtils.getProperty(metadata.get("IPTC"), "CopyrightNotice"));
        Assert.assertEquals("Description", PropertyUtils.getProperty(metadata.get("IPTC"), "CaptionAbstract"));

        Assert.assertEquals("20mm", PropertyUtils.getProperty(metadata.get("XMP"), "Lens"));
        Assert.assertEquals("1280614195", PropertyUtils.getProperty(metadata.get("XMP"), "SerialNumber"));

        Assert.assertEquals("https://creativecommons.org/licenses/by-sa/4.0/", PropertyUtils.getProperty(metadata.get("Photoshop"), "URL"));
    }
}
