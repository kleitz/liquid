package liquid.order.model;

import liquid.container.domain.ContainerSubtype;
import liquid.core.model.IdObject;
import liquid.operation.domain.Customer;
import liquid.operation.domain.Goods;
import liquid.operation.domain.Location;
import liquid.operation.domain.ServiceType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by Tao Ma on 2/24/15.
 */
public class BaseOrder extends IdObject {
    private String orderNo;
    private ServiceType serviceType;
    //    private Long customerId;
//
//    @NotNull
//    @NotEmpty
//    private String customerName;
    @NotNull
    private Customer customer;

    @NotNull
    private Location source;

    @NotNull
    private Location destination;
    private String consignee;
    private String consigneePhone;
    private String consigneeAddress;
    private Goods goods;

    @Min(1)
    @NotNull
    private Integer goodsWeight;
    private String goodsDimension;

    private Integer containerType = 0;
    private String containerTypeName;
    private ContainerSubtype containerSubtype;

    @NotNull
    @Min(1)
    private Integer containerQuantity = 0;
    private String containerAttribute;

    @Min(1)
    private BigDecimal cnyTotal;
    private BigDecimal usdTotal;

    private String createdBy;
    private String createdAt;
    private String updatedAt;

    private String role;
    private Integer status;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Location getSource() {
        return source;
    }

    public void setSource(Location source) {
        this.source = source;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
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

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Integer getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(Integer goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public String getGoodsDimension() {
        return goodsDimension;
    }

    public void setGoodsDimension(String goodsDimension) {
        this.goodsDimension = goodsDimension;
    }

    public Integer getContainerType() {
        return containerType;
    }

    public void setContainerType(Integer containerType) {
        this.containerType = containerType;
    }

    public String getContainerTypeName() {
        return containerTypeName;
    }

    public void setContainerTypeName(String containerTypeName) {
        this.containerTypeName = containerTypeName;
    }

    public ContainerSubtype getContainerSubtype() {
        return containerSubtype;
    }

    public void setContainerSubtype(ContainerSubtype containerSubtype) {
        this.containerSubtype = containerSubtype;
    }

    public Integer getContainerQuantity() {
        return containerQuantity;
    }

    public void setContainerQuantity(Integer containerQuantity) {
        this.containerQuantity = containerQuantity;
    }

    public String getContainerAttribute() {
        return containerAttribute;
    }

    public void setContainerAttribute(String containerAttribute) {
        this.containerAttribute = containerAttribute;
    }

    public BigDecimal getCnyTotal() {
        return cnyTotal;
    }

    public void setCnyTotal(BigDecimal cnyTotal) {
        this.cnyTotal = cnyTotal;
    }

    public BigDecimal getUsdTotal() {
        return usdTotal;
    }

    public void setUsdTotal(BigDecimal usdTotal) {
        this.usdTotal = usdTotal;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=BaseOrder");
        sb.append(", orderNo='").append(orderNo).append('\'');
        sb.append(", serviceType='").append(serviceType).append('\'');
        sb.append(", customer=").append(customer);
        sb.append(", source=").append(source);
        sb.append(", destination='").append(destination).append('\'');
        sb.append(", consignee='").append(consignee).append('\'');
        sb.append(", consigneePhone='").append(consigneePhone).append('\'');
        sb.append(", consigneeAddress='").append(consigneeAddress).append('\'');
        sb.append(", goods=").append(goods);
        sb.append(", goodsWeight=").append(goodsWeight);
        sb.append(", goodsDimension='").append(goodsDimension).append('\'');
        sb.append(", containerType=").append(containerType);
        sb.append(", containerTypeName='").append(containerTypeName).append('\'');
        sb.append(", containerSubytype='").append(containerSubtype).append('\'');
        sb.append(", containerSubtype='").append(containerSubtype).append('\'');
        sb.append(", containerQuantity=").append(containerQuantity);
        sb.append(", containerAttribute='").append(containerAttribute).append('\'');
        sb.append(", cnyTotal=").append(cnyTotal);
        sb.append(", usdTotal=").append(usdTotal);
        sb.append(", createdBy='").append(createdBy).append('\'');
        sb.append(", createdAt='").append(createdAt).append('\'');
        sb.append(", updatedAt='").append(updatedAt).append('\'');
        sb.append(", role='").append(role).append('\'');
        sb.append(", status=").append(status);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
