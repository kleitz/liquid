package liquid.transport.domain;

import javax.persistence.*;
import java.util.Date;

/**
 *  
 * User: tao
 * Date: 10/12/13
 * Time: 2:42 PM
 */
@MappedSuperclass
public class WaterContainer extends BaseLegContainer {
    /**
     * Bill of Lading No.
     */
    @Column(name = "BOL_NO")
    private String bolNo;

    @Column(name = "SLOT")
    private String slot;

    @Column(name = "ISSUED_AT")
    private Date issuedAt;

    /**
     * Estimated time of transport
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ETS")
    private Date ets;

    @Transient
    private String etsStr;

    @Transient
    private boolean batch;

    public String getBolNo() {
        return bolNo;
    }

    public void setBolNo(String bolNo) {
        this.bolNo = bolNo;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Date getEts() {
        return ets;
    }

    public void setEts(Date ets) {
        this.ets = ets;
    }

    public String getEtsStr() {
        return etsStr;
    }

    public void setEtsStr(String etsStr) {
        this.etsStr = etsStr;
    }

    public boolean isBatch() {
        return batch;
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WaterContainer{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", bolNo='").append(bolNo).append('\'');
        sb.append(", slot='").append(slot).append('\'');
        sb.append(", issuedAt=").append(issuedAt);
        sb.append(", ets=").append(ets);
        sb.append(", etsStr='").append(etsStr).append('\'');
        sb.append(", batch=").append(batch);
        sb.append('}');
        return sb.toString();
    }
}
