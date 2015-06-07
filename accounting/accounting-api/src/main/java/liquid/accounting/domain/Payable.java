package liquid.accounting.domain;

import liquid.core.domain.BaseUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tao Ma on 6/7/15.
 */
@Entity(name = "ACC_PAYABLE")
public class Payable extends BaseUpdateEntity {

    @ManyToOne
    @JoinColumn(name = "CHARGE_ID")
    private ChargeEntity charge;

    @Column(name = "APPLIED_AT")
    private Date appliedAt;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(precision = 19, scale = 4, name = "INVOICED_AMT")
    private BigDecimal invoicedAmt = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "PAID_AMT")
    private BigDecimal paidAmt = BigDecimal.ZERO;

    @Column(name = "PAID_AT")
    private Date paidAt;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getInvoicedAmt() {
        return invoicedAmt;
    }

    public void setInvoicedAmt(BigDecimal invoicedAmt) {
        this.invoicedAmt = invoicedAmt;
    }

    public BigDecimal getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(BigDecimal paidAmt) {
        this.paidAmt = paidAmt;
    }

    public Date getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Date paidAt) {
        this.paidAt = paidAt;
    }
}
