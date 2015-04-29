package liquid.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Tao Ma on 4/29/15.
 */
public class StringUtilTest {
    @Test
    public void testEncodeHex() {
        assertEquals("0102", StringUtil.encodeHex(new byte[]{1, 2}));
    }
}
