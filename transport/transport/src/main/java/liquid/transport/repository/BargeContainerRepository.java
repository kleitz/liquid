package liquid.transport.repository;

import liquid.order.domain.Order;
import liquid.transport.domain.BargeContainer;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 *  
 * User: tao
 * Date: 10/12/13
 * Time: 2:48 PM
 */
public interface BargeContainerRepository extends CrudRepository<BargeContainer, Long> {
    Collection<BargeContainer> findByOrder(Order order);

    Collection<BargeContainer> findByShipmentId(Long shipmentId);
}
