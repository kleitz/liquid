package liquid.transport.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import liquid.core.domain.BaseTaskEntity;
import liquid.order.domain.Order;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;

/**
 *  
 * User: tao
 * Date: 10/10/13
 * Time: 9:15 PM
 */
@Entity(name = "TSP_SHIPMENT")
public class ShipmentEntity extends BaseTaskEntity {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Min(1)
    @Column(name = "CONTAINER_QTY")
    private int containerQty;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "shipment", orphanRemoval = true)
    public List<LegEntity> legs;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shipment")
    private List<ShippingContainerEntity> containers;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shipment")
    private Collection<RailContainer> railContainers;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shipment")
    private Collection<BargeContainer> bargeContainers;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shipment")
    private Collection<VesselContainer> vesselContainers;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shipment")
    private Collection<DeliveryContainerEntity> deliveryContainers;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getContainerQty() {
        return containerQty;
    }

    public void setContainerQty(int containerQty) {
        this.containerQty = containerQty;
    }

    public List<LegEntity> getLegs() {
        return legs;
    }

    public void setLegs(List<LegEntity> legs) {
        this.legs = legs;
    }

    public List<ShippingContainerEntity> getContainers() {
        return containers;
    }

    public void setContainers(List<ShippingContainerEntity> containers) {
        this.containers = containers;
    }

    public Collection<RailContainer> getRailContainers() {
        return railContainers;
    }

    public void setRailContainers(Collection<RailContainer> railContainers) {
        this.railContainers = railContainers;
    }

    public Collection<BargeContainer> getBargeContainers() {
        return bargeContainers;
    }

    public void setBargeContainers(Collection<BargeContainer> bargeContainers) {
        this.bargeContainers = bargeContainers;
    }

    public Collection<VesselContainer> getVesselContainers() {
        return vesselContainers;
    }

    public void setVesselContainers(Collection<VesselContainer> vesselContainers) {
        this.vesselContainers = vesselContainers;
    }

    public Collection<DeliveryContainerEntity> getDeliveryContainers() {
        return deliveryContainers;
    }

    public void setDeliveryContainers(Collection<DeliveryContainerEntity> deliveryContainers) {
        this.deliveryContainers = deliveryContainers;
    }
}
