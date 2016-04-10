package liquid.transport.domain;

import liquid.core.domain.BaseUpdateEntity;
import liquid.operation.domain.ServiceProvider;
import liquid.order.domain.Order;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Tao Ma on 12/31/14.
 */
@Entity(name = "TSP_TRUCK")
public class Truck extends BaseUpdateEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Deprecated
    @ManyToOne
    @JoinColumn(name = "SHIPMENT_ID")
    private ShipmentEntity shipment;

    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProvider sp;

    @Column(name = "PICKING_AT")
    private Date pickingAt;

    @Column(name = "LICENSE_PLATE")
    private String licensePlate;

    @Column(name = "DRIVER")
    private String driver;

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

    public Date getPickingAt() {
        return pickingAt;
    }

    public void setPickingAt(Date pickingAt) {
        this.pickingAt = pickingAt;
    }

    public ServiceProvider getSp() {
        return sp;
    }

    public void setSp(ServiceProvider sp) {
        this.sp = sp;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Truck{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", order=").append(order);
        sb.append(", shipment=").append(shipment);
        sb.append(", sp=").append(sp);
        sb.append(", pickingAt=").append(pickingAt);
        sb.append(", licensePlate='").append(licensePlate).append('\'');
        sb.append(", driver='").append(driver).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
