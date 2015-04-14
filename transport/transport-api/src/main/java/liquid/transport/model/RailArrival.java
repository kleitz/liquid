package liquid.transport.model;

import liquid.util.DatePattern;
import liquid.validation.constraints.DateFormat;

/**
 *  
 * User: tao
 * Date: 10/19/13
 * Time: 3:42 PM
 */
public class RailArrival extends RailContainer {
    @DateFormat(DatePattern.UNTIL_MINUTE)
    private String ata;

    public String getAta() {
        return ata;
    }

    public void setAta(String ata) {
        this.ata = ata;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("RailArrivalDto{");
        sb.append("ata='").append(ata).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
