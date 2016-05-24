package liquid.accounting.repository;

import liquid.accounting.domain.Purchase;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by mat on 3/26/16.
 */
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
    List<Purchase> findByOrderId(Long orderId);

    List<Purchase> findBySpId(Long serviceProviderId);
}
