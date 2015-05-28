package liquid.order.repository;

import liquid.order.domain.ReceivingContainer;
import org.springframework.data.repository.CrudRepository;

/**
 *  
 * User: tao
 * Date: 10/13/13
 * Time: 6:34 PM
 */
public interface ReceivingContainerRepository extends CrudRepository<ReceivingContainer, Long> {
//    Collection<ReceivingContainerEntity> findByReceivingOrder(ReceivingOrderEntity recvOrder);
}
