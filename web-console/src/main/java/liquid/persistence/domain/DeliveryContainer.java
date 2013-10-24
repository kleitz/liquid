package liquid.persistence.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/24/13
 * Time: 10:52 PM
 */
@Entity(name = "DELIVERY_CONTAINER")
public class DeliveryContainer extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @OneToOne
    @JoinColumn(name = "SC_ID")
    private ShippingContainer sc;

    @Column(name = "ADDRESS")
    private String address;

    /**
     * Estimated time of delivery
     */
    @Column(name = "ETD")
    private Date etd;

    @Transient
    private String etdStr;

    @Transient
    private boolean batch;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ShippingContainer getSc() {
        return sc;
    }

    public void setSc(ShippingContainer sc) {
        this.sc = sc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public String getEtdStr() {
        return etdStr;
    }

    public void setEtdStr(String etdStr) {
        this.etdStr = etdStr;
    }

    public boolean isBatch() {
        return batch;
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("DeliveryContainer{");
        sb.append("order=").append(order);
        sb.append(", sc=").append(sc);
        sb.append(", address='").append(address).append('\'');
        sb.append(", etd=").append(etd);
        sb.append(", etdStr='").append(etdStr).append('\'');
        sb.append(", batch=").append(batch);
        sb.append('}');
        return sb.toString();
    }
}
