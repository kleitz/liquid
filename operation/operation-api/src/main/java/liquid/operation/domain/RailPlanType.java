package liquid.operation.domain;

import liquid.core.domain.BaseUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by redbrick9 on 8/23/14.
 */
@Entity(name = "OPS_RAIL_PLAN_TYPE")
public class RailPlanType extends BaseUpdateEntity {
    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS")
    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RailPlanType{");
        sb.append("name='").append(name).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
