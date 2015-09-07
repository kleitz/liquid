package liquid.accounting.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import liquid.core.domain.BaseUpdateEntity;
import liquid.operation.deserializer.ServiceSubtypeDeserializer;
import liquid.operation.domain.ServiceSubtype;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

/**
 * Created by Tao Ma on 9/6/15.
 */
@Entity(name = "ACC_INVOICE_ITEM")
public class InvoiceItem extends BaseUpdateEntity {
    @JsonDeserialize(using = ServiceSubtypeDeserializer.class)
    @ManyToOne
    @JoinColumn(name = "SERVICE_SUBTYPE_ID")
    private ServiceSubtype serviceSubtype;

    @Column(name = "QUANTITY")
    private Integer quantity = 1;

    @Column(precision = 19, scale = 4, name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "DESCRIPTION")
    private String description;

    public ServiceSubtype getServiceSubtype() {
        return serviceSubtype;
    }

    public void setServiceSubtype(ServiceSubtype serviceSubtype) {
        this.serviceSubtype = serviceSubtype;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
