package liquid.transport.service;

import liquid.core.service.AbstractService;
import liquid.transport.domain.Truck;
import liquid.transport.repository.TruckRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Tao Ma on 1/1/15.
 */
@Service
public class TruckServiceImpl extends AbstractService<Truck, TruckRepository>
        implements InternalTruckService {
    @Override
    public void doSaveBefore(Truck entity) {}

    @Deprecated
    @Override
    public Iterable<Truck> findByShipmentId(Long shipmentId) {
        return repository.findByShipmentId(shipmentId);
    }

    @Override
    public List<Truck> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }
}
