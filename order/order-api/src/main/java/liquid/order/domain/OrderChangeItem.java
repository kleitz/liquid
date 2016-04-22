package liquid.order.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by mat on 4/21/16.
 */
public enum OrderChangeItem {
    CONTAINER_QUANTITY("containerQty", "container.quantity");

    private final String fieldName;

    private final String i18nKey;

    private OrderChangeItem(String fieldName, String i18nKey) {
        this.fieldName = fieldName;
        this.i18nKey = i18nKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getI18nKey() {
        return i18nKey;
    }

    public static Map<String, String> toMap() {
        Map<String, String> map = new TreeMap<String, String>();
        OrderChangeItem[] items = values();
        for (OrderChangeItem item : items) {
            map.put(item.fieldName, item.i18nKey);
        }
        return map;
    }
 }
