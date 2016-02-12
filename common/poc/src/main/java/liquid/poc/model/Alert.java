package liquid.poc.model;

/**
 * Created by mat on 2/12/16.
 */
public class Alert {
    public enum Type{
        success, info, warning, danger
    }

    private Type type;
    private String message;

    public Alert() {
    }

    public Alert(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Alert{");
        sb.append("type=").append(type);
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
