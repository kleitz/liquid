package liquid.accounting.service;

import liquid.accounting.domain.ExchangeRate;
import liquid.accounting.repository.ExchangeRateRepository;
import liquid.core.service.AbstractCachedService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tao Ma on 1/14/15.
 */
@Service
public class ExchangeRateService extends AbstractCachedService<ExchangeRate, ExchangeRateRepository> {
    @Override
    public void doSaveBefore(ExchangeRate entity) {}

    public ExchangeRate getExchangeRate() {
        Iterable<ExchangeRate> exchangeRateIterable = findAll();
        for (ExchangeRate exchangeRate : exchangeRateIterable) {
            Calendar exchangeRateDate = Calendar.getInstance();
            exchangeRateDate.setTime(exchangeRate.getMonth());
            int exchangeRateYear = exchangeRateDate.get(Calendar.YEAR);
            int exchangeRateMonth = exchangeRateDate.get(Calendar.MONTH);
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH);
            if(exchangeRateYear == year && exchangeRateMonth == month) {
                return exchangeRate;
            }
        }
        return new ExchangeRate(ExchangeRate.DEFAULT_EXCHANGE_RATE);
    }

    public Iterable<ExchangeRate> findAll() {
        return repository.findAll();
    }

    public ExchangeRate findByMonth(Date month) {
        return repository.findByMonth(month);
    }
}
