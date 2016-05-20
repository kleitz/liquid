package liquid.accounting.repository;

import liquid.accounting.domain.Revenue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mat on 5/19/16.
 */
public interface RevenueRepository extends CrudRepository<Revenue, Long> {
    Revenue findByCustomerId(Long customerId);

    Page<Revenue> findAll(Pageable pageable);

    Page<Revenue> findAll(Specification<Revenue> specification, Pageable pageable);
}
