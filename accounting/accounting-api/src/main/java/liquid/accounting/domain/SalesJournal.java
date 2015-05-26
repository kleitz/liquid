package liquid.accounting.domain;

import liquid.core.domain.BaseIdEntity;
import liquid.order.domain.Order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tao Ma on 5/5/15.
 * Record sales income and ajusted income per order.
 */
@Entity(name = "ACC_SALES_JOURNAL")
public class SalesJournal extends BaseIdEntity {

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Column(name = "REVENUE_CNY")
    private BigDecimal revenueCny;

    @Column(name = "REVENUE_USD")
    private BigDecimal revenueUsd;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getRevenueCny() {
        return revenueCny;
    }

    public void setRevenueCny(BigDecimal revenueCny) {
        this.revenueCny = revenueCny;
    }

    public BigDecimal getRevenueUsd() {
        return revenueUsd;
    }

    public void setRevenueUsd(BigDecimal revenueUsd) {
        this.revenueUsd = revenueUsd;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
