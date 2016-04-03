package liquid.accounting.domain;

/**
 * Created by mat on 4/3/16.
 */
public enum PurchaseStatus {
    VALID,
    INVALID;

    public static void main(String[] args) {
        System.out.println(PurchaseStatus.INVALID.ordinal());
    }
}
