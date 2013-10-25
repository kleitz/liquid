package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/14/13
 * Time: 8:34 PM
 */
@MappedSuperclass
public class BaseOrder extends BaseEntity {
    @Transient
    private long customerId;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "SRC_LOC_ID")
    private Location srcLoc;

    @Transient
    private long origination;

    @ManyToOne
    @JoinColumn(name = "DST_LOC_ID")
    private Location dstLoc;

    @Transient
    private long destination;

    @NotNull @NotEmpty
    @Column(name = "CONSIGNEE")
    private String consignee;

    @NotNull @NotEmpty
    @Column(name = "CONSIGNEE_PHONE")
    private String consigneePhone;

    @NotNull @NotEmpty
    @Column(name = "CONSIGNEE_ADDR")
    private String consigneeAddress;

    @Transient
    private long cargoId;

    @OneToOne
    @JoinColumn(name = "CARGO_ID")
    private Cargo cargo;

    /**
     * unit kilogram
     */
    @Min(1)
    @Column(name = "WEIGHT")
    private int cargoWeight;

    @Column(name = "CONTAINER_TYPE")
    private int containerType;

    @Column(name = "CONTAINER_CAP")
    private int containerCap;

    @Min(1)
    @Column(name = "CONTAINER_QTY")
    private int containerQty;

    /**
     * unit yuan
     */
    @Min(1)
    @Column(name = "SALES_PRICE")
    private long salesPrice;

    @Column(name = "DISTY_PRICE")
    private long distyPrice;

    @Column(name = "EXT_EXP")
    private long extExp = 0L;

    @Column(name = "EXT_EXP_COMMENT")
    private String extExpComment;

    @Column(name = "CREATE_ROLE")
    private String createRole;

    // 1 saved; 2: submitted
    @Column(name = "STATUS")
    private int status;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Location getSrcLoc() {
        return srcLoc;
    }

    public void setSrcLoc(Location srcLoc) {
        this.srcLoc = srcLoc;
    }

    public long getOrigination() {
        return origination;
    }

    public void setOrigination(long origination) {
        this.origination = origination;
    }

    public Location getDstLoc() {
        return dstLoc;
    }

    public void setDstLoc(Location dstLoc) {
        this.dstLoc = dstLoc;
    }

    public long getDestination() {
        return destination;
    }

    public void setDestination(long destination) {
        this.destination = destination;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public long getCargoId() {
        return cargoId;
    }

    public void setCargoId(long cargoId) {
        this.cargoId = cargoId;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public int getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(int cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public int getContainerType() {
        return containerType;
    }

    public void setContainerType(int containerType) {
        this.containerType = containerType;
    }

    public int getContainerCap() {
        return containerCap;
    }

    public void setContainerCap(int containerCap) {
        this.containerCap = containerCap;
    }

    public int getContainerQty() {
        return containerQty;
    }

    public void setContainerQty(int containerQty) {
        this.containerQty = containerQty;
    }

    public long getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(long salesPrice) {
        this.salesPrice = salesPrice;
    }

    public long getDistyPrice() {
        return distyPrice;
    }

    public void setDistyPrice(long distyPrice) {
        this.distyPrice = distyPrice;
    }

    public long getExtExp() {
        return extExp;
    }

    public void setExtExp(long extExp) {
        this.extExp = extExp;
    }

    public String getExtExpComment() {
        return extExpComment;
    }

    public void setExtExpComment(String extExpComment) {
        this.extExpComment = extExpComment;
    }

    public String getCreateRole() {
        return createRole;
    }

    public void setCreateRole(String createRole) {
        this.createRole = createRole;
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
        sb.append("BaseOrder{");
        sb.append("customerId=").append(customerId);
        sb.append(", customer=").append(customer);
        sb.append(", srcLoc=").append(srcLoc);
        sb.append(", origination=").append(origination);
        sb.append(", dstLoc=").append(dstLoc);
        sb.append(", destination=").append(destination);
        sb.append(", consignee='").append(consignee).append('\'');
        sb.append(", consigneePhone='").append(consigneePhone).append('\'');
        sb.append(", consigneeAddress='").append(consigneeAddress).append('\'');
        sb.append(", cargoId=").append(cargoId);
        sb.append(", cargo=").append(cargo);
        sb.append(", cargoWeight=").append(cargoWeight);
        sb.append(", containerType=").append(containerType);
        sb.append(", containerCap=").append(containerCap);
        sb.append(", containerQty=").append(containerQty);
        sb.append(", salesPrice=").append(salesPrice);
        sb.append(", distyPrice=").append(distyPrice);
        sb.append(", extExp=").append(extExp);
        sb.append(", extExpComment='").append(extExpComment).append('\'');
        sb.append(", createRole='").append(createRole).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
