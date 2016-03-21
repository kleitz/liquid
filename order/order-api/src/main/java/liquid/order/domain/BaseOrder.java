package liquid.order.domain;

import liquid.container.domain.ContainerSubtype;
import liquid.core.domain.BaseUpdateEntity;
import liquid.operation.domain.Customer;
import liquid.operation.domain.Goods;
import liquid.operation.domain.Location;
import liquid.operation.domain.ServiceType;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * User: tao
 * Date: 10/14/13
 * Time: 8:34 PM
 */
@MappedSuperclass
public class BaseOrder extends BaseUpdateEntity {
    @ManyToOne
    @JoinColumn(name = "SERVICE_TYPE_ID")
    private ServiceType serviceType;

    @Column(name = "ORDER_NO")
    private String orderNo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "SRC_LOC_ID")
    private Location source;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "DST_LOC_ID")
    private Location destination;

    @Column(name = "CONSIGNEE")
    private String consignee;

    @Column(name = "CONSIGNEE_PHONE")
    private String consigneePhone;

    @Column(name = "CONSIGNEE_ADDR")
    private String consigneeAddress;

    @ManyToOne
    @JoinColumn(name = "GOODS_ID")
    private Goods goods;

    /**
     * unit ton
     */
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#.###")
    @Column(name = "WEIGHT")
    private BigDecimal goodsWeight;

    @Column(name = "DIMENSION")
    private String goodsDimension;

    @Column(name = "CONTAINER_TYPE")
    private int containerType;

    // The following three filed are used for list order
    @ManyToOne
    @JoinColumn(name = "CONTAINER_SUBTYPE_ID")
    private ContainerSubtype containerSubtype;

    @Column(name = "CONTAINER_CAP")
    private int containerCap;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#")
    @Column(name = "CONTAINER_QTY")
    private Integer containerQty;

    @Column(name = "CONTAINER_ATTR")
    private String containerAttribute;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "RAILWAY_ID")
    private OrderRail railway;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#,###.##")
    @Column(precision = 19, scale = 4, name = "TOTAL_CNY")
    private BigDecimal totalCny = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "TOTAL_USD")
    private BigDecimal totalUsd = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "DISTY_CNY")
    private BigDecimal distyCny = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "DISTY_USD")
    private BigDecimal distyUsd = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, name = "GRAND_TOTAL")
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Column(name = "CREATE_ROLE")
    private String createRole;

    // 1 saved; 2: submitted; 3
    @Column(name = "STATUS")
    private int status;

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public BigDecimal getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(BigDecimal goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public String getGoodsDimension() {
        return goodsDimension;
    }

    public void setGoodsDimension(String goodsDimension) {
        this.goodsDimension = goodsDimension;
    }

    public int getContainerType() {
        return containerType;
    }

    public void setContainerType(int containerType) {
        this.containerType = containerType;
    }

    public ContainerSubtype getContainerSubtype() {
        return containerSubtype;
    }

    public void setContainerSubtype(ContainerSubtype containerSubtype) {
        this.containerSubtype = containerSubtype;
    }

    public int getContainerCap() {
        return containerCap;
    }

    public void setContainerCap(Integer containerCap) {
        this.containerCap = containerCap;
    }

    public Integer getContainerQty() {
        return containerQty;
    }

    public void setContainerQty(int containerQty) {
        this.containerQty = containerQty;
    }

    public String getContainerAttribute() {
        return containerAttribute;
    }

    public void setContainerAttribute(String containerAttribute) {
        this.containerAttribute = containerAttribute;
    }

    public OrderRail getRailway() {
        return railway;
    }

    public void setRailway(OrderRail railway) {
        this.railway = railway;
    }

    public BigDecimal getTotalCny() {
        return totalCny;
    }

    public void setTotalCny(BigDecimal totalCny) {
        this.totalCny = totalCny;
    }

    public BigDecimal getTotalUsd() {
        return totalUsd;
    }

    public void setTotalUsd(BigDecimal totalUsd) {
        this.totalUsd = totalUsd;
    }

    public BigDecimal getDistyCny() {
        return distyCny;
    }

    public void setDistyCny(BigDecimal distyCny) {
        this.distyCny = distyCny;
    }

    public BigDecimal getDistyUsd() {
        return distyUsd;
    }

    public void setDistyUsd(BigDecimal distyUsd) {
        this.distyUsd = distyUsd;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
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
        sb.append(", serviceType=").append(serviceType);
        sb.append(", orderNo='").append(orderNo).append('\'');
        sb.append(", customer=").append(customer);
        sb.append(", source=").append(source);
        sb.append(", destination=").append(destination);
        sb.append(", consignee='").append(consignee).append('\'');
        sb.append(", consigneePhone='").append(consigneePhone).append('\'');
        sb.append(", consigneeAddress='").append(consigneeAddress).append('\'');
        sb.append(", goods=").append(goods);
        sb.append(", goodsWeight=").append(goodsWeight);
        sb.append(", goodsDimension='").append(goodsDimension).append('\'');
        sb.append(", containerType=").append(containerType);
        sb.append(", containerSubtype=").append(containerSubtype);
        sb.append(", containerCap=").append(containerCap);
        sb.append(", containerQty=").append(containerQty);
        sb.append(", containerAttribute='").append(containerAttribute).append('\'');
        sb.append(", railway=").append(railway);
        sb.append(", totalCny=").append(totalCny);
        sb.append(", totalUsd=").append(totalUsd);
        sb.append(", distyCny=").append(distyCny);
        sb.append(", distyUsd=").append(distyUsd);
        sb.append(", grandTotal=").append(grandTotal);
        sb.append(", createRole='").append(createRole).append('\'');
        sb.append(", status=").append(status);
        return sb.toString();
    }
}
