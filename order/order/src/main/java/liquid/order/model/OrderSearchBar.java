package liquid.order.model;

import liquid.core.model.Pagination;

/**
 * Created by Tao Ma on 5/25/15.
 */
public class OrderSearchBar extends Pagination {
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

    public void prepand(String uri) {
        setHref(uri + toQueryStrings());
    }

    private String toQueryStrings() {
        StringBuilder sb = new StringBuilder("?");
        if (null != type) sb.append("type=").append(type).append("&");
        if (null != id) sb.append("id=").append(id).append("&");
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=OrderSearchBar");
        sb.append(", id=").append(id);
        sb.append(", type='").append(type).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
