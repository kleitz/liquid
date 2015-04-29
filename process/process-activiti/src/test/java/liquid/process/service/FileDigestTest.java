package liquid.process.service;

import liquid.util.StringUtil;
import org.junit.Test;

import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Tao Ma on 4/29/15.
 */
public class FileDigestTest {

    @Test
    public void testDigest() throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (DigestInputStream in = new DigestInputStream(this.getClass().getClassLoader().getResourceAsStream("processes/liquid.poc.bpmn20.xml"), md)) {
            byte[] content = new byte[in.available()];
            in.read(content);
        }
        byte[] checksum = md.digest();
        System.out.println(StringUtil.encodeHex(checksum));
    }
}
