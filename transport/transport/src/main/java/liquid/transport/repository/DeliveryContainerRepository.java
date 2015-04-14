package liquid.transport.repository;

import liquid.order.domain.OrderEntity;
import liquid.transport.domain.DeliveryContainerEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 *  
 * User: tao
 * Date: 10/24/13
 * Time: 10:57 PM
 */
public interface DeliveryContainerRepository extends CrudRepository<DeliveryContainerEntity, Long> {
    Collection<DeliveryContainerEntity> findByOrder(OrderEntity order);
}
