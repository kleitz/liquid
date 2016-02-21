package liquid.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import liquid.core.domain.BaseIdEntity;
import liquid.operation.domain.Goods;
import liquid.operation.domain.Location;
import liquid.operation.domain.RailPlanType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by redbrick9 on 8/22/14.
 */
@Entity(name = "ORD_RAIL")
public class OrderRail extends BaseIdEntity {
    @Column(name = "PLAN_REPORT_TIME")
    private Date planReportTime;

    @ManyToOne
    @JoinColumn(name = "PLAN_TYPE")
    private RailPlanType planType;

    @Column(name = "PROGRAM_NO")
    private String programNo;

    @ManyToOne
    @JoinColumn(name = "PLAN_GOODS_ID")
    private Goods planGoods;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "SOURCE_ID")
    private Location source;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "DESTINATION_ID")
    private Location destination;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "SAME_DAY")
    private Boolean sameDay;

    public Date getPlanReportTime() {
        return planReportTime;
    }

    public void setPlanReportTime(Date planReportTime) {
        this.planReportTime = planReportTime;
    }

    public RailPlanType getPlanType() {
        return planType;
    }

    public void setPlanType(RailPlanType planType) {
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderRail{");
        sb.append(super.toString());
        sb.append(", planReportTime=").append(planReportTime);
        sb.append(", planType=").append(planType);
        sb.append(", programNo='").append(programNo).append('\'');
        sb.append(", planGoods=").append(planGoods);
        sb.append(", source=").append(source);
        sb.append(", destination=").append(destination);
        sb.append(", comment='").append(comment).append('\'');
        sb.append(", sameDay=").append(sameDay);
        sb.append('}');
        return sb.toString();
    }
}
