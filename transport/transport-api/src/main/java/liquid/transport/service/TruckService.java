package liquid.transport.service;

import liquid.transport.domain.Truck;

import java.util.List;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface TruckService {
    Truck find(Long id);

    @Deprecated
    Iterable<Truck> findByShipmentId(Long shipmentId);

    List<Truck> findByOrderId(Long orderId);

    Iterable<Truck> save(Iterable<Truck> truckEntities);
}
