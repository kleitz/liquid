package liquid.accounting.domain;

import liquid.core.domain.BaseUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * User: tao
 * Date: 11/16/13
 * Time: 1:32 PM
 */
@Entity(name = "ACC_EXCHANGE_RATE")
public class ExchangeRate extends BaseUpdateEntity {
    public static final BigDecimal DEFAULT_EXCHANGE_RATE = new BigDecimal("6.09");

    public ExchangeRate() { }

    public ExchangeRate(BigDecimal value) {
        this.value = value;
    }

    /**
     * 1 usd = ? cny
     */
    @Column(precision = 19, scale = 4, name = "VALUE")
    private BigDecimal value = DEFAULT_EXCHANGE_RATE;

    @Column(name = "MONTH")
    private Date month;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public static BigDecimal getDefaultExchangeRate() {
        return DEFAULT_EXCHANGE_RATE;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExchangeRate{");
        sb.append("super=").append(super.toString()).append('\'');
        sb.append(", value=").append(value);
        sb.append(", month=").append(month);
        sb.append('}');
        return sb.toString();
    }
}
