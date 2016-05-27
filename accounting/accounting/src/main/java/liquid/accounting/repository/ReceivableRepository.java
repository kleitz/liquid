package liquid.accounting.repository;

import liquid.accounting.domain.ReceivableSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mat on 5/19/16.
 */
public interface ReceivableRepository extends CrudRepository<ReceivableSummary, Long> {
    ReceivableSummary findByCustomerId(Long customerId);

    Page<ReceivableSummary> findAll(Pageable pageable);

    Page<ReceivableSummary> findAll(Specification<ReceivableSummary> specification, Pageable pageable);
}
