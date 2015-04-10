package liquid.accounting.service;

import liquid.accounting.domain.TaxRate;
import liquid.accounting.repository.TaxRateRepository;
import liquid.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by redbrick9 on 8/29/14.
 */
@Service
public class TaxRateServiceImpl extends AbstractService<TaxRate, TaxRateRepository>
        implements TaxRateService {

    @Autowired
    private TaxRateRepository taxRateRepository;

    @Override
    public void doSaveBefore(TaxRate entity) {}

    public Collection<TaxRate> findAll() {
        return taxRateRepository.findAll();
    }
}
