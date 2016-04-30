package liquid.order.domain;

import liquid.core.domain.BaseIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by mat on 4/28/16.
 */
@Entity(name = "ORD_CONTAINER_CHANGE")
public class OrderContainerChange extends BaseIdEntity {

    @Column(name = "ORDER_ID")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "ORDER_CONTAINER_ID")
    private OrderContainer container;

    @Column(name = "OLD_VALUE")
    private String oldValue;

    @Column(name = "NEW_VALUE")
    private String newValue;

    @Column(name = "CHANGED_AT")
    private Date changedAt;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public OrderContainer getContainer() {
        return container;
    }

    public void setContainer(OrderContainer container) {
        this.container = container;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Date getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Date changedAt) {
        this.changedAt = changedAt;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderContainerChange{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", orderId=").append(orderId);
        sb.append(", container=").append(container);
        sb.append(", oldValue='").append(oldValue).append('\'');
        sb.append(", newValue='").append(newValue).append('\'');
        sb.append(", changedAt=").append(changedAt);
        sb.append(", changedBy='").append(changedBy).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
