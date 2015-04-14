package liquid.operation.domain;

import liquid.core.domain.BaseUpdateEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 *  
 * User: tao
 * Date: 10/5/13
 * Time: 10:33 AM
 */
@Entity(name = "OPS_LOCATION")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Location extends BaseUpdateEntity {

    @Column(name = "NAME")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE_ID")
    private LocationType type;

    @Column(name = "Q_NAME")
    private String queryName;

    public Location() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }
}
