package liquid.order.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FIXME - Rename pick?
 * User: tao
 * Date: 10/13/13
 * Time: 4:08 PM
 */
@Deprecated
@Entity(name = "ORD_RECV")
public class ReceivingOrder extends BaseOrder {
    /**
     * HACK - Unidirectional OneToMany, No Inverse ManyToOne, No Join Table (JPA 2.0 ONLY).
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "RECV_ORDER_ID", referencedColumnName = "ID")
    private List<OrderContainer> containers = new ArrayList<>();

    public List<OrderContainer> getContainers() {
        return containers;
    }

    public void setContainers(List<OrderContainer> containers) {
        this.containers = containers;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=ReceivingOrderEntity");
        sb.append(", containers=").append(containers);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
