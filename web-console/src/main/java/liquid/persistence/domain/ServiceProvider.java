package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 4:38 PM
 */
@Entity(name = "SERVICE_PROVIDER")
public class ServiceProvider extends BaseEntity {
    @NotNull @NotEmpty
    @Column(name = "CODE")
    private String code;

    @NotNull @NotEmpty
    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private SpType type;

    @Transient
    private long typeId;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "POSTCODE")
    private String postcode;

    @Column(name = "CONTACT")
    private String contact;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "CELL")
    private String cell;

    /**
     * enabled or disabled
     */
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

    public SpType getType() {
        return type;
    }

    public void setType(SpType type) {
        this.type = type;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("ServiceProvider{");
        sb.append("code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", type=").append(type);
        sb.append(", typeId=").append(typeId);
        sb.append(", address='").append(address).append('\'');
        sb.append(", postcode='").append(postcode).append('\'');
        sb.append(", contact='").append(contact).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", cell='").append(cell).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
