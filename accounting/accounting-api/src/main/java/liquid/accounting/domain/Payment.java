package liquid.accounting.domain;

import liquid.core.domain.BaseUpdateEntity;
import liquid.operation.domain.ServiceProvider;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by mat on 5/24/16.
 */
@Entity(name = "ACC_PAYMENT")
public class Payment extends BaseUpdateEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProvider serviceProvider;

    @Column(precision = 19, scale = 4, name = "AMOUNT_CNY")
    private BigDecimal amountCny;

    @Column(precision = 19, scale = 4, name = "AMOUNT_USD")
    private BigDecimal amountUsd;

    @Column(name = "PAID_AT")
    private Date paidAt;

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public BigDecimal getAmountCny() {
        return amountCny;
    }

    public void setAmountCny(BigDecimal amountCny) {
        this.amountCny = amountCny;
    }

    public BigDecimal getAmountUsd() {
        return amountUsd;
    }

    public void setAmountUsd(BigDecimal amountUsd) {
        this.amountUsd = amountUsd;
    }

    public Date getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Date paidAt) {
        this.paidAt = paidAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Payment{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", serviceProvider=").append(serviceProvider);
        sb.append(", amountCny=").append(amountCny);
        sb.append(", amountUsd=").append(amountUsd);
        sb.append(", paidAt=").append(paidAt);
        sb.append('}');
        return sb.toString();
    }
}
