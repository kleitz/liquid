package liquid.accounting.domain;

import liquid.operation.domain.ServiceProvider;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Created by Tao Ma on 9/6/15.
 */
@Entity(name = "ACC_PURCHASE_INVOICE")
public class PurchaseInvoice extends BaseInvoice {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProvider serviceProvider;
}
