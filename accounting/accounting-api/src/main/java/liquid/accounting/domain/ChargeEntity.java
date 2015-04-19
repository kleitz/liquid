package liquid.accounting.domain;

import liquid.operation.domain.ServiceProvider;
import liquid.operation.domain.ServiceSubtype;
import liquid.order.domain.OrderEntity;
import liquid.core.domain.BaseUpdateEntity;
import liquid.transport.domain.LegEntity;
import liquid.transport.domain.ShipmentEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 *  
 * User: tao
 * Date: 10/2/13
 * Time: 7:43 PM
 */
@Entity(name = "ACT_CHARGE")
@EntityListeners(liquid.audit.AuditListener.class)
public class ChargeEntity extends BaseUpdateEntity {

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Column(name = "TASK_ID")
    private String taskId;

    @ManyToOne
    @JoinColumn(name = "SHIPMENT_ID")
    private ShipmentEntity shipment;

    @ManyToOne
    @JoinColumn(name = "LEG_ID")
    private LegEntity leg;

    @ManyToOne
    @JoinColumn(name = "SERVICE_SUBTYPE_ID")
    private ServiceSubtype serviceSubtype;

    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProvider sp;

    @Column(name = "WAY")
    private int way = 1;

    @Column(precision = 19, scale = 4, name = "UNIT_PRICE")
    private BigDecimal unitPrice;

    @Column(precision = 19, scale = 4, name = "TOTAL_PRICE")
    private BigDecimal totalPrice;

    @Column(name = "CURRENCY")
    private int currency;

    @Column(name = "CREATE_ROLE")
    private String createRole;

    @Column(name = "STATUS")
    private int status;

    @Column(name = "COMMENT")
    private String comment;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public LegEntity getLeg() {
        return leg;
    }

    public void setLeg(LegEntity leg) {
        this.leg = leg;
    }

    public ShipmentEntity getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentEntity shipment) {
        this.shipment = shipment;
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

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public String getCreateRole() {
        return createRole;
    }

    public void setCreateRole(String createRole) {
        this.createRole = createRole;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * For auditing.
     *
     * @return
     */
    public String toAudit() {
        final StringBuilder sb = new StringBuilder("ChargeEntity{");
        sb.append("id=").append(id);
        sb.append(", orderId=").append(order == null ? null : order.getId());
        sb.append(", taskId='").append(taskId).append('\'');
        sb.append(", shipmentId=").append(shipment == null ? null : shipment.getId());
        sb.append(", legId=").append(leg == null ? null : leg.getId());
        sb.append(", serviceSubtypeId=").append(serviceSubtype == null ? null : serviceSubtype.getId());
        sb.append(", spId=").append(sp == null ? null : sp.getId());
        sb.append(", way=").append(way);
        sb.append(", unitPrice=").append(unitPrice);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", currency=").append(currency);
        sb.append(", createRole='").append(createRole).append('\'');
        sb.append(", status=").append(status);
        sb.append(", comment=").append(comment);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String toString() {
        return toAudit();
    }
}
