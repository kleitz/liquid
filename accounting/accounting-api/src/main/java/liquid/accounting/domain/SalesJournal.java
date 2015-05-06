package liquid.accounting.domain;

import liquid.core.domain.BaseIdEntity;
import liquid.operation.domain.ServiceSubtype;
import liquid.order.domain.OrderEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tao Ma on 5/5/15.
 */
//@Entity(name = "ACC_SALES_JOURNAL")
public class SalesJournal extends BaseIdEntity {

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "SERVICE_TYPE_ID")
    private ServiceSubtype service;

    /**
     * CNY or USD
     */
    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "CREATED_AT")
    private Date createdAt;
}
