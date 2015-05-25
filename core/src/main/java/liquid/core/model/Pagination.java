package liquid.core.model;

/**
 * Created by Tao Ma on 5/25/15.
 */
public class Pagination {
    private String href;

    private int number;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=Pagination");
        sb.append(", href='").append(href).append('\'');
        sb.append(", number=").append(number);
        sb.append('}');
        return sb.toString();
    }
}
