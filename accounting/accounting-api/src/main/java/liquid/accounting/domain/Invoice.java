package liquid.accounting.domain;

import liquid.core.domain.BaseUpdateEntity;
import liquid.operation.domain.Customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tao Ma on 1/8/15.
 */
@Entity(name = "ACC_INVOICE")
public class Invoice extends BaseUpdateEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(precision = 19, scale = 4, name = "AMOUNT_CNY")
    private BigDecimal amountCny;

    @Column(precision = 19, scale = 4, name = "AMOUNT_USD")
    private BigDecimal amountUsd;

    @Column(name = "ISSUED_AT")
    private Date issuedAt;

    @Column(name = "EXP_PAY_AT")
    private Date expectedPaymentAt;

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Date getExpectedPaymentAt() {
        return expectedPaymentAt;
    }

    public void setExpectedPaymentAt(Date expectedPaymentAt) {
        this.expectedPaymentAt = expectedPaymentAt;
    }
}
