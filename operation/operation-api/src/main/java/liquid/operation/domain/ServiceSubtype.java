package liquid.operation.domain;

import liquid.core.converter.Text;
import liquid.core.domain.BaseUpdateEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Service Provider Type which means what kind of service the providers offer.
 * Created by redbrick9 on 5/9/14.
 */
@Entity(name = "OPS_SERVICE_SUBTYPE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServiceSubtype extends BaseUpdateEntity implements Text {
    @NotNull
    @NotEmpty
    @Column(name = "CODE")
    private String code;

    @NotNull
    @NotEmpty
    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS")
    private int status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
    public String toText() {
        return String.valueOf(id);
    }

    // FIXME - A workaround for rending checked value of multi-valued checkbox.
    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
