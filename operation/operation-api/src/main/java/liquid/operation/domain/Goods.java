package liquid.operation.domain;

import liquid.core.domain.BaseUpdateEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 *  
 * User: tao
 * Date: 9/28/13
 * Time: 3:27 PM
 */
@Entity(name = "OPS_GOODS")
public class Goods extends BaseUpdateEntity {
    @NotNull
    @NotEmpty
    @Column(name = "NAME", nullable = true, unique = true)
    private String name;

    @Column(name = "STATUS")
    private int status;

    public Goods() { }

    public Goods(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Goods{");
        sb.append("id='").append(getId()).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}