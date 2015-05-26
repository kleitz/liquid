package liquid.transport.repository;

import liquid.order.domain.Order;
import liquid.transport.domain.VesselContainer;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 *  
 * User: tao
 * Date: 10/12/13
 * Time: 4:27 PM
 */
public interface VesselContainerRepository extends CrudRepository<VesselContainer, Long> {
    Collection<VesselContainer> findByOrder(Order order);

    Collection<VesselContainer> findByShipmentId(Long shipmentId);
}
