package liquid.accounting.domain;

import liquid.core.domain.BaseUpdateEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by mat on 5/24/16.
 */
@MappedSuperclass
public class BaseInvoice extends BaseUpdateEntity {
    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(precision = 19, scale = 4, name = "AMOUNT_CNY")
    private BigDecimal amountCny;

    @Column(precision = 19, scale = 4, name = "AMOUNT_USD")
    private BigDecimal amountUsd;

    @Column(name = "ISSUED_AT")
    private Date issuedAt;

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BaseInvoice{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", invoiceNo='").append(invoiceNo).append('\'');
        sb.append(", amountCny=").append(amountCny);
        sb.append(", amountUsd=").append(amountUsd);
        sb.append(", issuedAt=").append(issuedAt);
        sb.append('}');
        return sb.toString();
    }
}
