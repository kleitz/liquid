package liquid.transport.repository;

import liquid.order.domain.OrderEntity;
import liquid.transport.domain.BargeContainerEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 *  
 * User: tao
 * Date: 10/12/13
 * Time: 2:48 PM
 */
public interface BargeContainerRepository extends CrudRepository<BargeContainerEntity, Long> {
    Collection<BargeContainerEntity> findByOrder(OrderEntity order);

    Collection<BargeContainerEntity> findByShipmentId(Long shipmentId);
}
