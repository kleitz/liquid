package liquid.operation.service;

import liquid.core.service.AbstractService;
import liquid.operation.domain.TaxRate;
import liquid.operation.domain.TaxRateStatus;
import liquid.operation.repository.TaxRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Collection<TaxRate> findEnabled() {
        return taxRateRepository.findByStatus(TaxRateStatus.ENABLED);
    }

    @Transactional(value = "transactionManager")
    @Override
    public TaxRate enable(Long id) {
        TaxRate taxRate = taxRateRepository.findOne(id);
        taxRate.setStatus(TaxRateStatus.ENABLED);
        return repository.save(taxRate);
    }

    @Transactional(value = "transactionManager")
    @Override
    public TaxRate disable(Long id) {
        TaxRate taxRate = taxRateRepository.findOne(id);
        taxRate.setStatus(TaxRateStatus.DISABLED);
        return repository.save(taxRate);
    }
}
