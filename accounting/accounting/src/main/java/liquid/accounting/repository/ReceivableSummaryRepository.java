package liquid.accounting.repository;

import liquid.accounting.domain.ReceivableSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 8/28/14.
 */
public interface ReceivableSummaryRepository extends CrudRepository<ReceivableSummary, Long> {
    ReceivableSummary findByOrderId(Long orderId);

    Page<ReceivableSummary> findAll(Pageable pageable);

    Page<ReceivableSummary> findAll(Specification<ReceivableSummary> specification, Pageable pageable);
}
