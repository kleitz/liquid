package liquid.accounting.service;

import liquid.accounting.domain.ExchangeRate;
import liquid.accounting.repository.ExchangeRateRepository;
import liquid.core.service.AbstractCachedService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Tao Ma on 1/14/15.
 */
@Service
public class ExchangeRateService extends AbstractCachedService<ExchangeRate, ExchangeRateRepository> {
    @Override
    public void doSaveBefore(ExchangeRate entity) { entity.setId(1L);}

    public ExchangeRate getExchangeRate() {
        ExchangeRate exchangeRate = find(1L);
        if (null == exchangeRate) return new ExchangeRate(ExchangeRate.DEFAULT_EXCHANGE_RATE);
        return exchangeRate;
    }

    public Iterable<ExchangeRate> findAll() {
        return repository.findAll();
    }

    public ExchangeRate findByMonth(Date month){
        return repository.findByMonth(month);
    }
}
