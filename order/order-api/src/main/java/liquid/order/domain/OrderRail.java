package liquid.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import liquid.core.domain.BaseIdEntity;
import liquid.operation.domain.Goods;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by redbrick9 on 8/22/14.
 */
@Entity(name = "ORD_RAIL")
public class OrderRail extends BaseIdEntity {
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Column(name = "PLAN_REPORT_TIME")
    private Date planReportTime;

    @Column(name = "PLAN_TYPE")
    private Long planType;

    @Column(name = "PROGRAM_NO")
    private String programNo;

    @ManyToOne
    @JoinColumn(name = "PLAN_GOODS_ID")
    private Goods planGoods;

    @Column(name = "SOURCE_ID")
    private Long sourceId;

    @Column(name = "DESTINATION_ID")
    private Long destinationId;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "SAME_DAY")
    private Boolean sameDay;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Date getPlanReportTime() {
        return planReportTime;
    }

    public void setPlanReportTime(Date planReportTime) {
        this.planReportTime = planReportTime;
    }

    public Long getPlanType() {
        return planType;
    }

    public void setPlanType(Long planType) {
        this.planType = planType;
    }

    public String getProgramNo() {
        return programNo;
    }

    public void setProgramNo(String programNo) {
        this.programNo = programNo;
    }

    public Goods getPlanGoods() {
        return planGoods;
    }

    public void setPlanGoods(Goods planGoods) {
        this.planGoods = planGoods;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getSameDay() {
        return sameDay;
    }

    public void setSameDay(Boolean sameDay) {
        this.sameDay = sameDay;
    }
}
