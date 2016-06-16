package liquid.order.domain;

/**
 *  
 * User: tao
 * Date: 9/29/13
 * Time: 10:16 PM
 */
public enum OrderStatus {
    NULL(0, ""),
    SAVED(1, "order.status.saved"),
    SUBMITTED(2, "order.status.submitted"),
    DISCARDED(3, "order.status.discarded"),
    COMPLETED(8, "order.status.completed"),
    PAID(9, "order.status.paid");

    private final int value;

    private final String i18nKey;

    private OrderStatus(int value, String i18nKey) {
        this.value = value;
        this.i18nKey = i18nKey;
    }

    public int getValue() {
        return value;
    }

    public String getI18nKey() {
        return i18nKey;
    }
}
