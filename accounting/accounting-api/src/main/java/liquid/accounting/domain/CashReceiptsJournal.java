package liquid.accounting.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import liquid.order.deserializer.OrderDeserializer;
import liquid.core.domain.BaseUpdateEntity;
import liquid.order.domain.Order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tao Ma on 5/6/15.
 */
@Entity(name = "ACC_CASH_RECEIPTS_JOURNAL")
public class CashReceiptsJournal extends BaseUpdateEntity implements Serializable {
    @JsonDeserialize(using = OrderDeserializer.class)
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Column(name = "QTY_OF_BOX")
    private Integer qtyOfBox;

    @Column(precision = 19, scale = 4, name = "REVENUE")
    private BigDecimal revenue;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "RECOGNIZED_AT")
    private Date recognizedAt;

    @Column(name = "STATUS")
    private Short status;

    @Column(precision = 19, scale = 4, name = "RECEIVED_AMT")
    private BigDecimal receivedAmt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "RECEIVED_AT")
    private Date receivedAt;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(precision = 19, scale = 4, name = "INVOICED_AMT")
    private BigDecimal invoicedAmt = BigDecimal.ZERO;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "INVOICED_AT")
    private Date invoicedAt;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getQtyOfBox() {
        return qtyOfBox;
    }

    public void setQtyOfBox(Integer qtyOfBox) {
        this.qtyOfBox = qtyOfBox;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public Date getRecognizedAt() {
        return recognizedAt;
    }

    public void setRecognizedAt(Date recognizedAt) {
        this.recognizedAt = recognizedAt;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public BigDecimal getReceivedAmt() {
        return receivedAmt;
    }

    public void setReceivedAmt(BigDecimal receivedAmt) {
        this.receivedAmt = receivedAmt;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Date receivedAt) {
        this.receivedAt = receivedAt;
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
        sb.append(", revenue=").append(revenue);
        sb.append(", recognizedAt=").append(recognizedAt);
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
