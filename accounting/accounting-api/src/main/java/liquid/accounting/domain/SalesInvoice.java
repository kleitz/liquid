package liquid.accounting.domain;

import liquid.operation.domain.Customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Tao Ma on 1/8/15.
 */
@Entity(name = "ACC_SALES_INVOICE")
public class SalesInvoice extends BaseInvoice {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @Column(name = "EXP_PAY_AT")
    private Date expectedPaymentAt;


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getExpectedPaymentAt() {
        return expectedPaymentAt;
    }

    public void setExpectedPaymentAt(Date expectedPaymentAt) {
        this.expectedPaymentAt = expectedPaymentAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SalesInvoice{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", customer=").append(customer);
        sb.append(", expectedPaymentAt=").append(expectedPaymentAt);
        sb.append('}');
        return sb.toString();
    }
}
