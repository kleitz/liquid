package liquid.accounting.repository;

import liquid.accounting.domain.Purchase;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by mat on 3/26/16.
 */
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
    List<Purchase> findByOrderId(Long orderId);

    @Deprecated
    List<Purchase> findBySpId(Long serviceProviderId);

    List<Purchase> findAll(Specification<Purchase> specification);
}