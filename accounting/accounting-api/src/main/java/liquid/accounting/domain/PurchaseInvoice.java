package liquid.accounting.domain;

import liquid.operation.domain.ServiceProvider;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Created by mat on 5/24/16.
 */
@Entity(name = "ACC_PURCHASE_INVOICE")
public class PurchaseInvoice extends BaseInvoice {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProvider serviceProvider;

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PurchaseInvoice{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", serviceProvider=").append(serviceProvider);
        sb.append('}');
        return sb.toString();
    }
}
