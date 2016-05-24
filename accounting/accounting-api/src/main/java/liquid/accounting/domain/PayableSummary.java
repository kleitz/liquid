package liquid.accounting.domain;

import liquid.core.domain.BaseUpdateEntity;
import liquid.operation.domain.ServiceProvider;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by mat on 5/23/16.
 */
@Entity(name = "ACC_PAYABLE_SUM")
public class PayableSummary extends BaseUpdateEntity {
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SP_ID")
    private ServiceProvider serviceProvider;

    @Column(precision = 19, scale = 4, name = "TOTAL_CNY")
    private BigDecimal totalCny = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "TOTAL_USD")
    private BigDecimal totalUsd = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "TOTAL_INVOICED_CNY")
    private BigDecimal totalInvoicedCny = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "TOTAL_INVOICED_USD")
    private BigDecimal totalInvoicedUsd = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "TOTAL_PAID_CNY")
    private BigDecimal totalPaidCny = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "TOTAL_PAID_USD")
    private BigDecimal totalPaidUsd = BigDecimal.ZERO;

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public BigDecimal getTotalCny() {
        return totalCny;
    }

    public void setTotalCny(BigDecimal totalCny) {
        this.totalCny = totalCny;
    }

    public BigDecimal getTotalUsd() {
        return totalUsd;
    }

    public void setTotalUsd(BigDecimal totalUsd) {
        this.totalUsd = totalUsd;
    }

    public BigDecimal getTotalInvoicedCny() {
        return totalInvoicedCny;
    }

    public void setTotalInvoicedCny(BigDecimal totalInvoicedCny) {
        this.totalInvoicedCny = totalInvoicedCny;
    }

    public BigDecimal getTotalInvoicedUsd() {
        return totalInvoicedUsd;
    }

    public void setTotalInvoicedUsd(BigDecimal totalInvoicedUsd) {
        this.totalInvoicedUsd = totalInvoicedUsd;
    }

    public BigDecimal getTotalPaidCny() {
        return totalPaidCny;
    }

    public void setTotalPaidCny(BigDecimal totalPaidCny) {
        this.totalPaidCny = totalPaidCny;
    }

    public BigDecimal getTotalPaidUsd() {
        return totalPaidUsd;
    }

    public void setTotalPaidUsd(BigDecimal totalPaidUsd) {
        this.totalPaidUsd = totalPaidUsd;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PayableSummary{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", serviceProvider=").append(serviceProvider);
        sb.append(", totalCny=").append(totalCny);
        sb.append(", totalUsd=").append(totalUsd);
        sb.append(", totalInvoicedCny=").append(totalInvoicedCny);
        sb.append(", totalInvoicedUsd=").append(totalInvoicedUsd);
        sb.append(", totalPaidCny=").append(totalPaidCny);
        sb.append(", totalPaidUsd=").append(totalPaidUsd);
        sb.append('}');
        return sb.toString();
    }
}
