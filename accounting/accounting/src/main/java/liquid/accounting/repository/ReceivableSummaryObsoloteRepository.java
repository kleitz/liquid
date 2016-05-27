package liquid.accounting.repository;

import liquid.accounting.domain.ReceivableSummaryObsolete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 8/28/14.
 */
public interface ReceivableSummaryObsoloteRepository extends CrudRepository<ReceivableSummaryObsolete, Long> {
    ReceivableSummaryObsolete findByOrderId(Long orderId);

    void deleteByOrderId(Long orderId);

    Page<ReceivableSummaryObsolete> findAll(Pageable pageable);

    Page<ReceivableSummaryObsolete> findAll(Specification<ReceivableSummaryObsolete> specification, Pageable pageable);
}
