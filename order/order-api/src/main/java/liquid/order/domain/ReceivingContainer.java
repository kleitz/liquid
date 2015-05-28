package liquid.order.domain;

import liquid.core.domain.BaseUpdateEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * User: tao
 * Date: 10/13/13
 * Time: 5:48 PM
 */
@Entity(name = "TSP_RECV_CONTAINER")
public class ReceivingContainer extends BaseUpdateEntity {
    @NotNull
    @NotEmpty
    @Column(name = "BIC_CODE")
    private String bicCode;

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=ReceivingContainerEntity");
        sb.append(", bicCode='").append(bicCode).append('\'');
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
