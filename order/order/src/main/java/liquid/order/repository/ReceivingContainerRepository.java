package liquid.order.repository;

import liquid.order.domain.OrderContainer;
import org.springframework.data.repository.CrudRepository;

/**
 *  
 * User: tao
 * Date: 10/13/13
 * Time: 6:34 PM
 */
public interface ReceivingContainerRepository extends CrudRepository<OrderContainer, Long> {
//    Collection<ReceivingContainerEntity> findByReceivingOrder(ReceivingOrderEntity recvOrder);
}
