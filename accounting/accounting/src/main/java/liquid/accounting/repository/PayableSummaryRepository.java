package liquid.accounting.repository;

import liquid.accounting.domain.PayableSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mat on 5/23/16.
 */
public interface PayableSummaryRepository extends CrudRepository<PayableSummary, Long> {
    PayableSummary findByServiceProviderId(Long serviceProviderId);
    Page<PayableSummary> findAll(Pageable pageable);

    Page<PayableSummary> findAll(Specification<PayableSummary> specification, Pageable pageable);
}
