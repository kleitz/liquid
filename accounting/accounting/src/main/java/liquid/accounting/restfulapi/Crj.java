package liquid.accounting.restfulapi;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tao Ma on 5/14/15.
 */
public class Crj {
    private String order;

    private Integer qtyOfBox;

    private BigDecimal revenue;

    private Date recognizedAt;

    private String status;

    private String receivedAmt;

    private String receivedAt;

    private String invoiceNo;

    private String invoicedAmt;

    private String invoicedAt;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceivedAmt() {
        return receivedAmt;
    }

    public void setReceivedAmt(String receivedAmt) {
        this.receivedAmt = receivedAmt;
    }

    public String getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(String receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoicedAmt() {
        return invoicedAmt;
    }

    public void setInvoicedAmt(String invoicedAmt) {
        this.invoicedAmt = invoicedAmt;
    }

    public String getInvoicedAt() {
        return invoicedAt;
    }

    public void setInvoicedAt(String invoicedAt) {
        this.invoicedAt = invoicedAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=Crj");
        sb.append(", order='").append(order).append('\'');
        sb.append(", qtyOfBox='").append(qtyOfBox).append('\'');
        sb.append(", revenue='").append(revenue).append('\'');
        sb.append(", recognizedAt='").append(recognizedAt).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", receivedAmt='").append(receivedAmt).append('\'');
        sb.append(", receivedAt='").append(receivedAt).append('\'');
        sb.append(", invoiceNo='").append(invoiceNo).append('\'');
        sb.append(", invoicedAmt='").append(invoicedAmt).append('\'');
        sb.append(", invoicedAt='").append(invoicedAt).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
