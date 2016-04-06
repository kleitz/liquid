package liquid.order.domain;

import liquid.core.domain.BaseUpdateEntity;
import liquid.operation.domain.Location;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * Created by mat on 4/6/16.
 */
@MappedSuperclass
public class ModifiableOrder extends BaseUpdateEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "SRC_LOC_ID")
    private Location source;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "DST_LOC_ID")
    private Location destination;

    public Location getSource() {
        return source;
    }

    public void setSource(Location source) {
        this.source = source;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#")
    @Column(name = "CONTAINER_QTY")
    private Integer containerQty;

    public Integer getContainerQty() {
        return containerQty;
    }

    public void setContainerQty(int containerQty) {
        this.containerQty = containerQty;
    }

    public void setContainerQty(Integer containerQty) {
        this.containerQty = containerQty;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModifiableOrder{");
        sb.append("source=").append(source);
        sb.append(", destination=").append(destination);
        sb.append(", containerQty=").append(containerQty);
        sb.append('}');
        return sb.toString();
    }
}
