package liquid.service;

import liquid.core.domain.TaxRateEntity;
import liquid.core.repository.TaxRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by redbrick9 on 8/29/14.
 */
@Service
public class TaxRateService extends AbstractService<TaxRateEntity, TaxRateRepository> {

    @Autowired
    private TaxRateRepository taxRateRepository;

    @Override
    public void doSaveBefore(TaxRateEntity entity) {}

    public Collection<TaxRateEntity> findAll() {
        return taxRateRepository.findAll();
    }
}
