package liquid.util;

import java.util.List;

/**
 *  
 * User: tao
 * Date: 10/12/13
 * Time: 9:18 PM
 */
public class CollectionUtil {
    private CollectionUtil() {}

    public static <T> T tryToGet2ndElement(List<T> list) {
        int size = list.size();
        long id = 0;
        if (size < 2) {
            return list.get(0);
        } else {
            return list.get(1);
        }
    }

    public static <T> boolean isEmpty(List<T> list) {
        if (null == list) return true;
        if (list.size() == 0) return true;
        return false;
    }
}
