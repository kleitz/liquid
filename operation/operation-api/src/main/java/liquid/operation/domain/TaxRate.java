package liquid.operation.domain;

import liquid.core.domain.BaseUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by redbrick9 on 8/29/14.
 */
@Entity(name = "ACC_TAX_RATE")
public class TaxRate extends BaseUpdateEntity {
    @Column(name = "NAME")
    private String name;

    @Column(name = "VALUE")
    private Integer value;

    @Column(name = "STATUS")
    private TaxRateStatus status;

    @Column(name = "COMMENT")
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public TaxRateStatus getStatus() {
        return status;
    }

    public void setStatus(TaxRateStatus status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TaxRate{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", value=").append(value);
        sb.append(", status=").append(status);
        sb.append(", comment='").append(comment).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
