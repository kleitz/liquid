package liquid.core.model;

import liquid.util.DateUtil;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mat on 10/18/14.
 */
public class SearchBarForm extends Pagination {
    @Deprecated
    private String action;
    @Deprecated
    private String types[][];

    /**
     * order id or customer id.
     */
    private Long id;

    /**
     * order or customer
     */
    private String type;

    /**
     * type ahead
     */
    private String text;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    public SearchBarForm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 0, 0, 0, 0);
        endDate = calendar.getTime();
        calendar.set(calendar.get(Calendar.YEAR), calendar.JANUARY, 1, 0, 0, 0);
        startDate = calendar.getTime();
    }

    @Deprecated
    public String getAction() {
        return action;
    }

    @Deprecated
    public void setAction(String action) {
        this.action = action;
    }

    @Deprecated
    public String[][] getTypes() {
        return types;
    }

    @Deprecated
    public void setTypes(String[][] types) {
        this.types = types;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void prepand(String uri) {
        setHref(uri + toQueryStrings());
    }

    public String toQueryStrings() {
        StringBuilder sb = new StringBuilder("?");
        sb.append("startDate=").append(DateUtil.stringOf(this.getStartDate())).append("&");
        sb.append("endDate=").append(DateUtil.stringOf(this.getEndDate())).append("&");
        sb.append("type=").append(null == this.getType() ? "" : this.getType()).append("&");
        sb.append("id=").append(null == this.getId() ? "" : this.getId()).append("&");
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=SearchBarForm");
        sb.append(", action='").append(action).append('\'');
        sb.append(", types=").append(Arrays.toString(types));
        sb.append(", id=").append(id);
        sb.append(", type='").append(type).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append('}');
        return sb.toString();
    }
}
