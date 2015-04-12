package liquid.transport.service;

import liquid.transport.domain.ShipmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface InternalShipmentService extends ShipmentService {

    Page<ShipmentEntity> findAll(Pageable pageable);

    void delete(Long id);
}
