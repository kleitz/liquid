package liquid.accounting.domain;

import liquid.core.domain.BaseUpdateEntity;
import liquid.operation.domain.Customer;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by mat on 5/19/16.
 */
@Entity(name = "ACC_REVENUE")
public class Revenue extends BaseUpdateEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @Column(precision = 19, scale = 4, name = "TOTAL_CNY")
    private BigDecimal totalCny = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "TOTAL_USD")
    private BigDecimal totalUsd = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "TOTAL_INVOICED_CNY")
    private BigDecimal totalInvoicedCny = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "TOTAL_INVOICED_USD")
    private BigDecimal totalInvoicedUsd = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "TOTAL_RECEIVED_CNY")
    private BigDecimal totalReceivedCny = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "TOTAL_RECEIVED_USD")
    private BigDecimal totalReceivedUsd = BigDecimal.ZERO;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public BigDecimal getTotalReceivedCny() {
        return totalReceivedCny;
    }

    public void setTotalReceivedCny(BigDecimal totalReceivedCny) {
        this.totalReceivedCny = totalReceivedCny;
    }

    public BigDecimal getTotalReceivedUsd() {
        return totalReceivedUsd;
    }

    public void setTotalReceivedUsd(BigDecimal totalReceivedUsd) {
        this.totalReceivedUsd = totalReceivedUsd;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Revenue{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", customer=").append(customer);
        sb.append(", totalCny=").append(totalCny);
        sb.append(", totalUsd=").append(totalUsd);
        sb.append(", totalInvoicedCny=").append(totalInvoicedCny);
        sb.append(", totalInvoicedUsd=").append(totalInvoicedUsd);
        sb.append(", totalReceivedCny=").append(totalReceivedCny);
        sb.append(", totalReceivedUsd=").append(totalReceivedUsd);
        sb.append('}');
        return sb.toString();
    }
}
