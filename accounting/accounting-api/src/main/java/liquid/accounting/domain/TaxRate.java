package liquid.accounting.domain;

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
}
