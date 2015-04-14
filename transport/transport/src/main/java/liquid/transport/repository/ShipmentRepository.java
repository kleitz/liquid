package liquid.transport.repository;

import liquid.core.repository.PageRepository;
import liquid.transport.domain.ShipmentEntity;
import org.springframework.data.repository.CrudRepository;

/**
 *  
 * User: tao
 * Date: 10/11/13
 * Time: 12:18 AM
 */
public interface ShipmentRepository extends CrudRepository<ShipmentEntity, Long>, PageRepository<ShipmentEntity> {
    Iterable<ShipmentEntity> findByOrderId(Long orderId);
}
