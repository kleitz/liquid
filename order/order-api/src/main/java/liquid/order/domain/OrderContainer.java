package liquid.order.domain;

import liquid.core.domain.BaseUpdateEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * User: tao
 * Date: 10/13/13
 * Time: 5:48 PM
 */
@Entity(name = "ORD_CONTAINER")
public class OrderContainer extends BaseUpdateEntity {
    @NotNull
    @NotEmpty
    @Column(name = "BIC_CODE")
    private String bicCode;

    @Override
    public String getCreatedBy() {
        return super.getCreatedBy();
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name= "REPLACED_BY")
    private OrderContainer replacedBy;

    @Column(name = "REPLACED_AT")
    private Date replacedAt;

    @Column(name = "REPLACED_REASON")
    private String replacedReason;

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public OrderContainer getReplacedBy() {
        return replacedBy;
    }

    public void setReplacedBy(OrderContainer replacedBy) {
        this.replacedBy = replacedBy;
    }

    public Date getReplacedAt() {
        return replacedAt;
    }

    public void setReplacedAt(Date replacedAt) {
        this.replacedAt = replacedAt;
    }

    public String getReplacedReason() {
        return replacedReason;
    }

    public void setReplacedReason(String replacedReason) {
        this.replacedReason = replacedReason;
    }

    @Override
    public String toString() {
        return "OrderContainer{" +
                "bicCode='" + bicCode + '\'' +
                ", replacedBy=" + replacedBy +
                ", replacedAt=" + replacedAt +
                ", replacedReason='" + replacedReason + '\'' +
                '}';
    }
}
