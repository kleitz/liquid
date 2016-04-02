package liquid.transport.repository;

import liquid.transport.domain.ShippingContainer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *  
 * User: tao
 * Date: 10/12/13
 * Time: 12:42 AM
 */
public interface ShippingContainerRepository extends CrudRepository<ShippingContainer, Long> {
    List<ShippingContainer> findByShipmentId(Long shipmentId);
}
