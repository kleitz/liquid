package liquid.transport.repository;

import liquid.order.domain.Order;
import liquid.transport.domain.RailContainer;
import liquid.transport.domain.ShipmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Date;

/**
 * User: tao
 * Date: 10/12/13
 * Time: 10:23 AM
 */
public interface RailContainerRepository extends CrudRepository<RailContainer, Long> {
    Collection<RailContainer> findByOrder(Order order);

    Collection<RailContainer> findByShipment(ShipmentEntity shipment);

    Collection<RailContainer> findByShipmentId(Long shipmentId);

    Iterable<RailContainer> findByReleasedAtGreaterThan(Date releasedAt);

    Page<RailContainer> findAll(Pageable pageable);
}
