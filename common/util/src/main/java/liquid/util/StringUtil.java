package liquid.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * User: tao
 * Date: 10/12/13
 * Time: 9:43 PM
 */
public class StringUtil {
    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private StringUtil() {}

    public static boolean valuable(String value) {
        return value != null && value.trim().length() > 0;
    }

    public static String toString(String[] values) {
        if (null == values) return "null";
        if (0 == values.length) return "";
        if (1 == values.length) return values[0];

        StringBuilder sb = new StringBuilder();
        sb.append(values[0]);
        for (int i = 1; i < values.length; i++) {
            sb.append(",");
            sb.append(values[i]);
        }
        return sb.toString();
    }

    public static boolean valid(String value) {
        return (null == value || 0 == value.trim().length()) ? false : true;
    }

    public static String utf8encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encodeHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
