package liquid.container.domain;

import liquid.operation.domain.Location;
import liquid.operation.domain.ServiceProvider;
import liquid.core.domain.BaseUpdateEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *  
 * User: tao
 * Date: 10/1/13
 * Time: 2:36 PM
 */
@Entity(name = "UNT_CONTAINER")
public class ContainerEntity extends BaseUpdateEntity {
    @NotNull
    @NotEmpty
    @Column(name = "BIC_CODE")
    private String bicCode;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private ServiceProvider owner;

    @ManyToOne
    @JoinColumn(name = "SUBTYPE_ID")
    private ContainerSubtypeEntity subtype;

    @ManyToOne
    @JoinColumn(name = "YARD_ID")
    private Location yard;

    @Column(name = "MOVE_IN_TIME")
    private Date moveInTime;

    @Column(name = "LOAD_TIME")
    private Date loadTime;

    @Column(name = "MOVE_OUT_TIME")
    private Date moveOutTime;

    @Column(name = "STATUS")
    private int status;

    @Column(name = "COMMENT")
    private String comment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContainerEntity that = (ContainerEntity) o;

        if (!bicCode.equals(that.bicCode)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return bicCode.hashCode();
    }

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public ServiceProvider getOwner() {
        return owner;
    }

    public void setOwner(ServiceProvider owner) {
        this.owner = owner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ContainerSubtypeEntity getSubtype() {
        return subtype;
    }

    public void setSubtype(ContainerSubtypeEntity subtype) {
        this.subtype = subtype;
    }

    public Location getYard() {
        return yard;
    }

    public void setYard(Location yard) {
        this.yard = yard;
    }

    public Date getMoveInTime() {
        return moveInTime;
    }

    public void setMoveInTime(Date moveInTime) {
        this.moveInTime = moveInTime;
    }

    public Date getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Date loadTime) {
        this.loadTime = loadTime;
    }

    public Date getMoveOutTime() {
        return moveOutTime;
    }

    public void setMoveOutTime(Date moveOutTime) {
        this.moveOutTime = moveOutTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
