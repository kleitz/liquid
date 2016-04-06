package liquid.order.repository;

import liquid.order.domain.ReceivingOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * User: tao
 * Date: 10/13/13
 * Time: 4:31 PM
 */
@Deprecated
public interface ReceivingOrderRepository extends CrudRepository<ReceivingOrder, Long>, JpaRepository<ReceivingOrder, Long> {
    Iterable<ReceivingOrder> findByOrderNoLike(String orderNo);

    Page<ReceivingOrder> findByOrderNoLike(String orderNo, Pageable pageable);

    Page<ReceivingOrder> findByCustomerIdAndCreatedBy(Long customerId, String createdBy, Pageable pageable);

    /**
     * Criteria Query
     *
     * @param specification
     * @param pageable
     * @return
     */
    Page<ReceivingOrder> findAll(Specification<ReceivingOrder> specification, Pageable pageable);
}