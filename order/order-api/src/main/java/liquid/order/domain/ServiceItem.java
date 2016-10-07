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

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#.###")
    @Column(name = "TAX")
    private BigDecimal tax;

    /**
     * PIT, Price Inclusive of Tax
     */
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#.###")
    @Column(name = "PRICE_INCL_OF_TAX")
    private BigDecimal priceInclOfTax;

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

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getPriceInclOfTax() {
        return priceInclOfTax;
    }

    public void setPriceInclOfTax(BigDecimal priceInclOfTax) {
        this.priceInclOfTax = priceInclOfTax;
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
        sb.append(", tax=").append(tax);
        sb.append(", priceInclOfTax=").append(priceInclOfTax);
        sb.append(", comment='").append(comment).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
