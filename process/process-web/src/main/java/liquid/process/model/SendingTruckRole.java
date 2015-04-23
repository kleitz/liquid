package liquid.process.model;

import java.io.Serializable;

/**
 * User: tao
 * Date: 10/23/13
 * Time: 12:14 AM
 * FIXME - Will delete it after GA.
 */
@Deprecated
public class SendingTruckRole implements Serializable {
    private String role = "SALES";

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=SendingTruckRole");
        sb.append(", role='").append(role).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
