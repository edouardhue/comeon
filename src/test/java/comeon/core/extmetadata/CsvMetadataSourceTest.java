package comeon.core.extmetadata;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CsvMetadataSourceTest {

    private CsvMetadataSource source;

    @Before
    public void setup() throws URISyntaxException {
        final URL resource = Resources.getResource("duplicates.csv");
        final Path testFile = Paths.get(resource.toURI());
        source = new CsvMetadataSource("file.name", "refphot", testFile, ';', '"', '\\', 0, false, true, Charsets.UTF_8, null);
    }

    @Test
    public void testReadBeansWithDuplicates() throws IOException {
        final List<Object> beans = source.readBeans();
        assertNotNull(beans);
        assertEquals(2, beans.size());
    }

    @Test(expected = DuplicateKeyException.class)
    public void testLoadMetadataWithDuplicates() throws IOException {
        source.loadMetadata();
    }
}
