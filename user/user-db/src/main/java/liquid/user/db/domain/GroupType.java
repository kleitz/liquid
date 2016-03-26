package liquid.user.db.domain;

/**
 * Created by mat on 3/26/16.
 */
public enum GroupType {
    ROLE_ADMIN(1),
    ROLE_SALES(2),
    ROLE_MARKETING(3),
    ROLE_COMMERCE(4),
    ROLE_OPERATING(5),
    ROLE_CONTAINER(6),
    ROLE_FIELD(7);

    private int value;

    private GroupType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
