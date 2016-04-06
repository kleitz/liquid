package liquid.order.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by mat on 4/5/16.
 */
@Entity(name = "ORD_CHANGE")
public class OrderChange extends ModifiableOrder {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
