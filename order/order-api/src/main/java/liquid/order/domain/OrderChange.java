package liquid.order.domain;

import liquid.core.domain.BaseIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by mat on 4/5/16.
 */
@Entity(name = "ORD_CHANGE")
public class OrderChange extends BaseIdEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Column(name = "ITEM")
    private String item;

    @Column(name = "OLD_VALUE")
    private String oldValue;

    @Column(name = "NEW_VALUE")
    private String newValue;

    @Column(name = "CHANGED_AT")
    private Date changedAt;

    @Column(name = "CHANGED_BY")
    private Date changedBy;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
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

    public Date getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Date changedBy) {
        this.changedBy = changedBy;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderChange{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", order=").append(order);
        sb.append(", item='").append(item).append('\'');
        sb.append(", oldValue='").append(oldValue).append('\'');
        sb.append(", newValue='").append(newValue).append('\'');
        sb.append(", changedAt=").append(changedAt);
        sb.append(", changedBy=").append(changedBy);
        sb.append('}');
        return sb.toString();
    }
}
