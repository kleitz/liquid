package liquid.transport.service;

import liquid.service.AbstractService;
import liquid.transport.domain.RailContainerEntity;
import liquid.transport.repository.RailContainerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collection;

/**
 * Created by Tao Ma on 1/1/15.
 */
@Service
public class RailContainerServiceImpl extends AbstractService<RailContainerEntity, RailContainerRepository>
        implements RailContainerService {
    @Override
    public void doSaveBefore(RailContainerEntity entity) { }

    @Override
    public Collection<RailContainerEntity> findByShipmentId(Long shipmentId) {
        return repository.findByShipmentId(shipmentId);
    }

    @Override
    public Iterable<RailContainerEntity> findByReleasedAtToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
        return repository.findByReleasedAtGreaterThan(calendar.getTime());
    }

    @Override
    public Page<RailContainerEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
