package liquid.accounting.repository;

import liquid.accounting.domain.Revenue;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mat on 5/19/16.
 */
public interface RevenueRepository extends CrudRepository<Revenue, Long> {
    Revenue findByCustomerId(Long customerId);
}
