package liquid.accounting.domain;

/**
 *  
 * User: tao
 * Date: 10/2/13
 * Time: 8:39 PM
 */
public enum ChargeWay {
    PER_CONTAINER(1, "charge.per.container"),
    PER_ORDER(0, "charge.per.order");

    private final int value;

    private final String i18nKey;

    private ChargeWay(int value, String i18nKey) {
        this.value = value;
        this.i18nKey = i18nKey;
    }

    public String getI18nKey() {
        return i18nKey;
    }

    public int getValue() {
        return value;
    }
}
