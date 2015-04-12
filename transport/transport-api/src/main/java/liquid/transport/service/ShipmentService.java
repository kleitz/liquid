package liquid.transport.service;

import liquid.transport.domain.ShipmentEntity;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface ShipmentService {
    ShipmentEntity find(Long id);

    Iterable<ShipmentEntity> findByOrderId(Long orderId);

    ShipmentEntity save(ShipmentEntity shipmentEntity);
}
