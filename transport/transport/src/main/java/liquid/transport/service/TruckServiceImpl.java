package liquid.transport.service;

import liquid.core.service.AbstractService;
import liquid.transport.domain.TruckEntity;
import liquid.transport.repository.TruckRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 1/1/15.
 */
@Service
public class TruckServiceImpl extends AbstractService<TruckEntity, TruckRepository>
        implements InternalTruckService {
    @Override
    public void doSaveBefore(TruckEntity entity) {}

    public Iterable<TruckEntity> findByShipmentId(Long shipmentId) {
        return repository.findByShipmentId(shipmentId);
    }
}
