package liquid.accounting.service;

import liquid.accounting.domain.ExchangeRateEntity;
import liquid.accounting.repository.ExchangeRateRepository;
import liquid.service.AbstractCachedService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by Tao Ma on 1/14/15.
 */
@Service
public class ExchangeRateService extends AbstractCachedService<ExchangeRateEntity, ExchangeRateRepository> {
    @Override
    public void doSaveBefore(ExchangeRateEntity entity) {}

    public BigDecimal getExchangeRate() {
        ExchangeRateEntity exchangeRate = find(1L);
        return null == exchangeRate ? ExchangeRateEntity.DEFAULT_EXCHANGE_RATE : exchangeRate.getValue();
    }

    public void setExchangeRate(BigDecimal value) {
        ExchangeRateEntity exchangeRate = new ExchangeRateEntity();
        exchangeRate.setId(1L);
        exchangeRate.setValue(value);
        save(exchangeRate);
    }
}
