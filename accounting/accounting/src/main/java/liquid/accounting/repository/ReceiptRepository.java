package liquid.accounting.repository;

import liquid.accounting.domain.ReceiptEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Tao Ma on 1/8/15.
 */
public interface ReceiptRepository extends CrudRepository<ReceiptEntity, Long> {
    Iterable<ReceiptEntity> findByOrderId(Long orderId);

    Iterable<ReceiptEntity> findByPayerId(Long payerId);
}
