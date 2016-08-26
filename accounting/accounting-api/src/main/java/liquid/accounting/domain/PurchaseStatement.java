package liquid.accounting.domain;

import liquid.core.domain.BaseUpdateEntity;
import liquid.operation.domain.ServiceProvider;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mat on 7/17/16.
 */
@Entity(name = "ACC_PURCHASE_STATEMENT")
public class PurchaseStatement extends BaseUpdateEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProvider serviceProvider;

    @NotNull
    @Column(name = "CODE")
    private String code;

    @Column(precision = 19, scale = 4, name = "TOTAL_CNY")
    private BigDecimal totalCny = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "TOTAL_USD")
    private BigDecimal totalUsd = BigDecimal.ZERO;

    @Column(name = "COMMENT")
    private String comment;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ACC_PURCHASE_STATEMENT_PURCHASE",
            joinColumns = {@JoinColumn(name = "STATEMENT_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "PURCHASE_ID", referencedColumnName = "ID")}
    )
    private List<Purchase> purchases = new ArrayList<>();

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PurchaseStatement{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", code=").append(code);
        sb.append(", serviceProvider=").append(serviceProvider);
        sb.append(", totalCny=").append(totalCny);
        sb.append(", totalUsd=").append(totalUsd);
        sb.append(", comment='").append(comment).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
