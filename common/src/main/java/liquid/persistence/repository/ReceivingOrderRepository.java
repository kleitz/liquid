package liquid.persistence.repository;

import liquid.persistence.domain.ReceivingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 4:31 PM
 */
public interface ReceivingOrderRepository extends CrudRepository<ReceivingOrder, Long>, JpaRepository<ReceivingOrder, Long> {
    Iterable<ReceivingOrder> findByOrderNoLike(String orderNo);

    Iterable<ReceivingOrder> findByCustomerNameLike(String cumtomerName);
}