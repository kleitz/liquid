package liquid.accounting.domain;

import liquid.operation.domain.Customer;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Created by Tao Ma on 9/6/15.
 */
@Entity(name = "ACC_SALES_INVOICE")
public class SalesInvoice extends BaseInvoice {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;
}
