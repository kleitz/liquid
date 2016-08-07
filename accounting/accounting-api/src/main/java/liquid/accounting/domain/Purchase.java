package liquid.accounting.domain;

import liquid.core.domain.BaseUpdateEntity;
import liquid.operation.domain.Currency;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.domain.ServiceSubtype;
import liquid.order.domain.Order;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by mat on 3/26/16.
 */
@Entity(name = "ACC_PURCHASE")
@EntityListeners(liquid.audit.AuditListener.class)
public class Purchase extends BaseUpdateEntity {

    @Column(name = "PURCHASE_NO")
    private String purchaseNo;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Column(name = "TRANSPORT_MODE")
    private Integer transportMode;

    @ManyToOne
    @JoinColumn(name = "SERVICE_SUBTYPE_ID")
    private ServiceSubtype serviceSubtype;

    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProvider sp;

    /**
     * Per order or per container.
     */
    @Column(name = "CHARGE_TYPE")
    private Integer chargeType;

    @Column(precision = 19, scale = 4, name = "UNIT_PRICE")
    private BigDecimal unitPrice;

    @Column(precision = 19, scale = 4, name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column(name = "CURRENCY", length = 4)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "STATUS")
    private PurchaseStatus status;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "TASK_ID")
    private String taskId;


    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(Integer transportMode) {
        this.transportMode = transportMode;
    }

    public ServiceSubtype getServiceSubtype() {
        return serviceSubtype;
    }

    public void setServiceSubtype(ServiceSubtype serviceSubtype) {
        this.serviceSubtype = serviceSubtype;
    }

    public ServiceProvider getSp() {
        return sp;
    }

    public void setSp(ServiceProvider sp) {
        this.sp = sp;
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "purchaseNo=" + purchaseNo +
                ", transportMode=" + transportMode +
                ", chargeType=" + chargeType +
                ", unitPrice=" + unitPrice +
                ", totalAmount=" + totalAmount +
                ", currency='" + currency + '\'' +
                ", status=" + status +
                ", comment='" + comment + '\'' +
                ", taskId='" + taskId + '\'' +
                '}';
    }
}