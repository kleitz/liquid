package liquid.operation.repository;

import liquid.operation.domain.TaxRate;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * Created by redbrick9 on 8/29/14.
 */
public interface TaxRateRepository extends CrudRepository<TaxRate, Long> {
    public Collection<TaxRate> findAll();
}
