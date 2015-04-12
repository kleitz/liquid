package liquid.transport.service;

import liquid.transport.domain.TruckEntity;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface TruckService {
    TruckEntity find(Long id);

    Iterable<TruckEntity> findByShipmentId(Long shipmentId);

    Iterable<TruckEntity> save(Iterable<TruckEntity> truckEntities);
}
