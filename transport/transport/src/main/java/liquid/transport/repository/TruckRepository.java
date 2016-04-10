package liquid.transport.repository;

import liquid.transport.domain.Truck;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Tao Ma on 1/1/15.
 */
public interface TruckRepository extends CrudRepository<Truck, Long> {
    @Deprecated
    Iterable<Truck> findByShipmentId(Long shipmentId);

    List<Truck> findByOrderId(Long orderId);
}
