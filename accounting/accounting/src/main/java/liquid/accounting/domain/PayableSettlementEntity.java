package liquid.accounting.domain;

import liquid.operation.domain.ServiceProvider;
import liquid.order.domain.Order;
import liquid.core.domain.BaseUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by mat on 11/15/14.
 */
@Entity(name = "ACC_PAYABLE_SETTLE")
public class PayableSettlementEntity extends BaseUpdateEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Column(name = "APPLIED_AT")
    private Date appliedAt;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(name = "CNY_OF_INVOICE")
    private Long cnyOfInvoice;

    @Column(name = "USD_OF_INVOICE")
    private Long usdOfInvoice;

    @ManyToOne
    @JoinColumn(name = "PAYEE_ID")
    private ServiceProvider payee;

    @Column(name = "CNY")
    private Long cny;

    @Column(name = "USD")
    private Long usd;

    @Column(name = "PAID_AT")
    private Date paidAt;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Date getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Date appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Long getCnyOfInvoice() {
        return cnyOfInvoice;
    }

    public void setCnyOfInvoice(Long cnyOfInvoice) {
        this.cnyOfInvoice = cnyOfInvoice;
    }

    public Long getUsdOfInvoice() {
        return usdOfInvoice;
    }

    public void setUsdOfInvoice(Long usdOfInvoice) {
        this.usdOfInvoice = usdOfInvoice;
    }

    public ServiceProvider getPayee() {
        return payee;
    }

    public void setPayee(ServiceProvider payee) {
        this.payee = payee;
    }

    public Long getCny() {
        return cny;
    }

    public void setCny(Long cny) {
        this.cny = cny;
    }

    public Long getUsd() {
        return usd;
    }

    public void setUsd(Long usd) {
        this.usd = usd;
    }

    public Date getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Date paidAt) {
        this.paidAt = paidAt;
    }
}

