package liquid.accounting.domain;

import liquid.core.domain.BaseUpdateEntity;
import liquid.operation.domain.Customer;
import liquid.order.domain.Order;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mat on 6/5/16.
 */
@Entity(name = "ACC_SALES_STATEMENT")
public class SalesStatement extends BaseUpdateEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @Column(precision = 19, scale = 4, name = "TOTAL_CNY")
    private BigDecimal totalCny;

    @Column(precision = 19, scale = 4, name = "TOTAL_USD")
    private BigDecimal totalUsd;

    @Column(name = "COMMENT")
    private String comment;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ACC_SALES_STATEMENT_ORDER",
            joinColumns = {@JoinColumn(name = "STATEMENT_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ORDER_ID", referencedColumnName = "ID", unique = true)}
    )
    private List<Order> orders;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getTotalCny() {
        return totalCny;
    }

    public void setTotalCny(BigDecimal totalCny) {
        this.totalCny = totalCny;
    }

    public BigDecimal getTotalUsd() {
        return totalUsd;
    }

    public void setTotalUsd(BigDecimal totalUsd) {
        this.totalUsd = totalUsd;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SalesStatement{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", customer=").append(customer);
        sb.append(", totalCny=").append(totalCny);
        sb.append(", totalUsd=").append(totalUsd);
        sb.append(", comment='").append(comment).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
