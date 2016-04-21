package liquid.order.domain;

/**
 * Created by mat on 4/21/16.
 */
public enum ChangeItem {
    CONTAINER_QUANTITY("containerQty", "container.quantity");

    private final String fieldName;

    private final String i18nKey;

    private ChangeItem(String fieldName, String i18nKey) {
        this.fieldName = fieldName;
        this.i18nKey = i18nKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getI18nKey() {
        return i18nKey;
    }
}
