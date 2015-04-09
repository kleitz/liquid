package liquid.common.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Created by redbrick9 on 4/26/14.
 */
@Entity(name = "SYS_SEQUENCE")
public class Sequence extends BaseUpdateEntity {
    @NotNull
    @NotEmpty
    @Column(name = "NAME")
    private String name;

    @NotNull
    @NotEmpty
    @Column(name = "PREFIX")
    private String prefix;

    @NotNull
    @NotEmpty
    @Column(name = "VALUE")
    private long value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Sequence{");
        sb.append("name='").append(name).append('\'');
        sb.append(", prefix='").append(prefix).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
