package comeon.core.extmetadata;

import static org.junit.Assert.*;

import org.junit.Test;

public class KeyTransformerTest {

  @Test
  public void testIdentityTransform() {
    final KeyTransformer t = new KeyTransformer(".*", "${0}");
    final String transformed = t.transform("AAAA0001.jpg");
    assertEquals("AAAA0001.jpg", transformed);
  }

  @Test
  public void testStripExtensionTransform() {
    final KeyTransformer t = new KeyTransformer("(.*)\\.jpg", "${1}");
    final String transformed = t.transform("AAAA0001.jpg");
    assertEquals("AAAA0001", transformed);
  }

  @Test
  public void testStripExtensionAndAppendTransform() {
    final KeyTransformer t = new KeyTransformer("(.*)(\\.jpg)", "ComeOn-${1}-Test${2}");
    final String transformed = t.transform("AAAA0001.jpg");
    assertEquals("ComeOn-AAAA0001-Test.jpg", transformed);
  }

}
