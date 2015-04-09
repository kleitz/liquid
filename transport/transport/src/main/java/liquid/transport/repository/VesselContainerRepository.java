package liquid.transport.repository;

import liquid.order.domain.OrderEntity;
import liquid.transport.domain.VesselContainerEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 4:27 PM
 */
public interface VesselContainerRepository extends CrudRepository<VesselContainerEntity, Long> {
    Collection<VesselContainerEntity> findByOrder(OrderEntity order);

    Collection<VesselContainerEntity> findByShipmentId(Long shipmentId);
}
