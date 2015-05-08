package liquid.accounting.domain;

import liquid.core.domain.BaseUpdateEntity;
import liquid.order.domain.OrderEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tao Ma on 5/6/15.
 */
@Entity(name = "ACC_CACH_RECEIPTS_JOURNAL")
public class CashReceiptsJournal extends BaseUpdateEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Column(name = "QTY_OF_BOX")
    private Integer qtyOfBox;

    @Column(precision = 19, scale = 4, name = "PAID_AMT")
    private BigDecimal paidAmt;

    @Column(name = "SETTLED_AT")
    private Date settledAt;

    @Column(name = "STATUS")
    private Short status;

    @Column(name = "RECEIVED_AT")
    private Date receivedAt;

    @Column(precision = 19, scale = 4, name = "RECEIVED_AMT")
    private BigDecimal receivedAmt;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(precision = 19, scale = 4, name = "INVOICED_AMT")
    private BigDecimal invoicedAmt;

    @Column(name = "INVOICED_AT")
    private Date invoicedAt;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Integer getQtyOfBox() {
        return qtyOfBox;
    }

    public void setQtyOfBox(Integer qtyOfBox) {
        this.qtyOfBox = qtyOfBox;
    }

    public BigDecimal getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(BigDecimal paidAmt) {
        this.paidAmt = paidAmt;
    }

    public Date getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(Date settledAt) {
        this.settledAt = settledAt;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Date receivedAt) {
        this.receivedAt = receivedAt;
    }

    public BigDecimal getReceivedAmt() {
        return receivedAmt;
    }

    public void setReceivedAmt(BigDecimal receivedAmt) {
        this.receivedAmt = receivedAmt;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public BigDecimal getInvoicedAmt() {
        return invoicedAmt;
    }

    public void setInvoicedAmt(BigDecimal invoicedAmt) {
        this.invoicedAmt = invoicedAmt;
    }

    public Date getInvoicedAt() {
        return invoicedAt;
    }

    public void setInvoicedAt(Date invoicedAt) {
        this.invoicedAt = invoicedAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=CashReceiptsJournal");
        sb.append(", order=").append(order == null ? "" : order.getId());
        sb.append(", qtyOfBox=").append(qtyOfBox);
        sb.append(", paidAmt=").append(paidAmt);
        sb.append(", settledAt=").append(settledAt);
        sb.append(", status=").append(status);
        sb.append(", receivedAt=").append(receivedAt);
        sb.append(", receivedAmt=").append(receivedAmt);
        sb.append(", invoiceNo='").append(invoiceNo).append('\'');
        sb.append(", invoicedAmt=").append(invoicedAmt);
        sb.append(", invoicedAt=").append(invoicedAt);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
