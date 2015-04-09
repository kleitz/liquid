package liquid.accounting.repository;

import liquid.accounting.domain.PayableSettlementEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mat on 11/15/14.
 */
public interface PayableSettlementRepository extends CrudRepository<PayableSettlementEntity, Long> {
    Iterable<PayableSettlementEntity> findByOrderId(Long orderId);
}
