package liquid.purchase.domain;

import liquid.core.domain.BaseUpdateEntity;
import liquid.operation.domain.ServiceProvider;
import liquid.order.domain.ServiceItem;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 8/27/15.
 */
@Entity(name = "PUR_ORDER")
public class PurchaseOrder extends BaseUpdateEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProvider serviceProvider;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    private List<ServiceItem> serviceItems = new ArrayList<>();

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public List<ServiceItem> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(List<ServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
    }
}
