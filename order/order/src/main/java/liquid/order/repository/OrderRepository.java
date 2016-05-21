package liquid.order.repository;

import liquid.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *  
 * User: tao
 * Date: 9/28/13
 * Time: 5:19 PM
 */
public interface OrderRepository extends CrudRepository<Order, Long>, JpaRepository<Order, Long> {
    /**
     * Criteria Query
     *
     * @param specification
     * @param pageable
     * @return
     */
    Page<Order> findAll(Specification<Order> specification, Pageable pageable);

    /**
     * TODO: Low Efficiency
     *
     * @param uid
     * @param pageable
     * @return
     */
    @Query(value = "SELECT o FROM ORD_ORDER o LEFT JOIN FETCH o.railway r WHERE o.createdBy = ?1",
            countQuery = "SELECT count(o) FROM ORD_ORDER o WHERE o.createdBy = ?1")
    Page<Order> findByCreatedBy(String uid, Pageable pageable);

    /**
     * TODO: Low Efficiency
     *
     * @param status
     * @param pageable
     * @return
     */
    Page<Order> findByStatus(Integer status, Pageable pageable);

    Page<Order> findByOrderNoLike(String orderNo, Pageable pageable);

    Iterable<Order> findByOrderNoLike(String orderNo);

    Page<Order> findByCustomerIdAndCreatedBy(Long customerId, String createdBy, Pageable pageable);

    List<Order> findByCustomerId(Long customerId);
}