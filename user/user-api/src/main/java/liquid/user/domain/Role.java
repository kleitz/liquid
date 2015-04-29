package liquid.user.domain;

/**
 * User: tao
 * Date: 10/23/13
 * Time: 12:21 AM
 */
public enum Role {
    ADMIN("ADMIN", "role.admin"),
    SALES("SALES", "role.sales"),
    MARKETING("MARKETING", "role.marketing"),
    OPERATING("OPERATING", "role.operating"),
    CONTAINER("CONTAINER", "role.container"),
    COMMERCE("COMMERCE", "role.commerce");

    private final String value;

    private final String i18nKey;

    private Role(String value, String i18nKey) {
        this.value = value;
        this.i18nKey = i18nKey;
    }

    public String getValue() {
        return value;
    }

    public String getI18nKey() {
        return i18nKey;
    }
}
