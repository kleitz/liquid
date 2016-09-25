package liquid.order.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import liquid.core.domain.BaseIdEntity;
import liquid.operation.deserializer.ServiceSubtypeDeserializer;
import liquid.operation.domain.Currency;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.domain.TaxRate;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by redbrick9 on 5/7/14.
 */
@Entity(name = "ORD_SERVICE_ITEM")
public class ServiceItem extends BaseIdEntity {
    @JsonDeserialize(using = ServiceSubtypeDeserializer.class)
    @ManyToOne
    @JoinColumn(name = "SERVICE_SUBTYPE_ID")
    private ServiceSubtype serviceSubtype;

    @Column(name = "CURRENCY", length = 4)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#.###")
    @Column(name = "QUOTATION")
    private BigDecimal quotation;

    @ManyToOne
    @JoinColumn(name = "TAX_RATE_ID")
    private TaxRate taxRate;

    /**
     * Tax inclusive
     */
    @Column(name = "TOTAL_INCL_TAX")
    private BigDecimal totalInclTax;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "STATUS")
    private Integer status;

    public ServiceSubtype getServiceSubtype() {
        return serviceSubtype;
    }

    public void setServiceSubtype(ServiceSubtype serviceSubtype) {
        this.serviceSubtype = serviceSubtype;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getQuotation() {
        return quotation;
    }

    public void setQuotation(BigDecimal quotation) {
        this.quotation = quotation;
    }

    public TaxRate getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(TaxRate taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTotalInclTax() {
        return totalInclTax;
    }

    public void setTotalInclTax(BigDecimal totalInclTax) {
        this.totalInclTax = totalInclTax;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ServiceItem{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", serviceSubtype=").append(serviceSubtype);
        sb.append(", currency=").append(currency);
        sb.append(", quotation=").append(quotation);
        sb.append(", taxRate=").append(taxRate);
        sb.append(", totalInclTax=").append(totalInclTax);
        sb.append(", comment='").append(comment).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
