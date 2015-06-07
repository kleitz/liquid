package liquid.accounting.repository;

import liquid.accounting.domain.Charge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *  
 * User: tao
 * Date: 10/2/13
 * Time: 8:37 PM
 */
public interface ChargeRepository extends CrudRepository<Charge, Long>  {
    List<Charge> findByTaskId(String taskId);

    Iterable<Charge> findByOrderId(Long orderId);

    Page<Charge> findByOrderId(Long orderId, Pageable pageable);

    Iterable<Charge> findByOrderOrderNoLike(String orderNo);

    Iterable<Charge> findBySpNameLike(String cumtomerName);

    Page<Charge> findBySpNameLike(String cumtomerName, Pageable pageable);

    Iterable<Charge> findByLegId(Long legId);

    Iterable<Charge> findByShipmentId(Long shipmentId);

    Iterable<Charge> findByOrderIdAndCreateRole(Long orderId, String createRole);

    Page<Charge> findAll(Pageable pageable);

    Page<Charge> findAll(Specification<Charge> specification, Pageable pageable);

    void deleteByLegId(Long legId);
}
