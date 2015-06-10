package liquid.accounting.domain;

import liquid.order.domain.Order;
import liquid.core.domain.BaseUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by Tao Ma on 1/10/15.
 */
@Entity(name = "ACC_SETTLEMENT")
public class SettlementEntity extends BaseUpdateEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Column(name = "CNY")
    private Long cny;

    @Column(name = "USD")
    private Long usd;

    @Column(name = "SETTLED_AT")
    private Date settledAt;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getCny() {
        return cny;
    }

    public void setCny(Long cny) {
        this.cny = cny;
    }

    public Long getUsd() {
        return usd;
    }

    public void setUsd(Long usd) {
        this.usd = usd;
    }

    public Date getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(Date settledAt) {
        this.settledAt = settledAt;
    }
}
