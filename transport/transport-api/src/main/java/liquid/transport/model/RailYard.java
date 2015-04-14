package liquid.transport.model;

import liquid.util.DatePattern;
import liquid.validation.constraints.DateFormat;

/**
 *  
 * User: tao
 * Date: 10/19/13
 * Time: 1:07 PM
 */
public class RailYard extends RailContainer {
    @DateFormat(DatePattern.UNTIL_MINUTE)
    private String railYardToa;

    public String getRailYardToa() {
        return railYardToa;
    }

    public void setRailYardToa(String railYardToa) {
        this.railYardToa = railYardToa;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("RailYardDto{");
        sb.append("railYardToa='").append(railYardToa).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
