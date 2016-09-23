package liquid.operation.service;

import liquid.operation.domain.TaxRate;

import java.util.Collection;

/**
 * Created by Tao Ma on 4/10/15.
 */
public interface TaxRateService {
    Collection<TaxRate> findAll();

    Iterable<TaxRate> save(Iterable<TaxRate> taxRates);
}
