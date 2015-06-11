package liquid.order.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import liquid.core.domain.BaseIdEntity;
import liquid.operation.deserializer.ServiceSubtypeDeserializer;
import liquid.operation.domain.Currency;
import liquid.operation.domain.ServiceSubtype;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
    private Currency currency;

    @Column(name = "QUOTATION")
    private Long quotation;

    @Column(name = "COMMENT")
    private String comment;

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

    public Long getQuotation() {
        return quotation;
    }

    public void setQuotation(Long quotation) {
        this.quotation = quotation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
