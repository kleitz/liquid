package liquid.transport.domain;

import liquid.container.domain.ContainerEntity;
import liquid.core.domain.BaseUpdateEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *  
 * User: tao
 * Date: 10/10/13
 * Time: 8:15 PM
 */
@Entity(name = "TSP_CONTAINER")
public class ShippingContainerEntity extends BaseUpdateEntity {
    @Column(name = "BIC_CODE")
    private String bicCode;

    @ManyToOne
    @JoinColumn(name = "CONTAINER_ID")
    private ContainerEntity container;

    @Transient
    private long containerId;

    @ManyToOne
    @JoinColumn(name = "SHIPMENT_ID")
    private ShipmentEntity shipment;

    @NotNull
    @NotEmpty
    @Column(name = "PICKUP_CONTACT")
    private String pickupContact;

    @NotNull
    @NotEmpty
    @Column(name = "CONTACT_PHONE")
    private String contactPhone;

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public ContainerEntity getContainer() {
        return container;
    }

    public void setContainer(ContainerEntity container) {
        this.container = container;
    }

    public long getContainerId() {
        return containerId;
    }

    public void setContainerId(long containerId) {
        this.containerId = containerId;
    }

    public ShipmentEntity getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentEntity shipment) {
        this.shipment = shipment;
    }

    public String getPickupContact() {
        return pickupContact;
    }

    public void setPickupContact(String pickupContact) {
        this.pickupContact = pickupContact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("ShippingContainer{");
        sb.append("container=").append(container);
        sb.append(", containerId=").append(containerId);
        sb.append(", shipment=").append(shipment);
        sb.append(", pickupContact='").append(pickupContact).append('\'');
        sb.append(", contactPhone='").append(contactPhone).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
