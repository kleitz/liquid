package liquid.transport.persistence.repository;

import liquid.core.PageRepository;
import liquid.transport.domain.ShipmentEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/11/13
 * Time: 12:18 AM
 */
public interface ShipmentRepository extends CrudRepository<ShipmentEntity, Long>, PageRepository<ShipmentEntity> {
    Iterable<ShipmentEntity> findByOrderId(Long orderId);
}
