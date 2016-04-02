package liquid.transport.domain;

import liquid.order.domain.Order;
import liquid.core.domain.BaseUpdateEntity;
import liquid.order.domain.OrderContainer;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

/**
 *  
 * User: tao
 * Date: 10/14/13
 * Time: 8:10 PM
 */
@MappedSuperclass
public class BaseLegContainer extends BaseUpdateEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Deprecated
    @ManyToOne
    @JoinColumn(name = "ROUTE_ID")
    private ShipmentEntity shipment;

    @Deprecated
    @ManyToOne
    @JoinColumn(name = "LEG_ID")
    private LegEntity leg;

    @Deprecated
    @OneToOne
    @JoinColumn(name = "SC_ID")
    private ShippingContainer sc;

    @OneToOne
    @JoinColumn(name = "CONTAINER_ID")
    private OrderContainer container;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ShipmentEntity getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentEntity shipment) {
        this.shipment = shipment;
    }

    public LegEntity getLeg() {
        return leg;
    }

    public void setLeg(LegEntity leg) {
        this.leg = leg;
    }

    public ShippingContainer getSc() {
        return sc;
    }

    public void setSc(ShippingContainer sc) {
        this.sc = sc;
    }

    public OrderContainer getContainer() {
        return container;
    }

    public void setContainer(OrderContainer container) {
        this.container = container;
    }
}
