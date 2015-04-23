package liquid.transport.service;

import liquid.core.service.AbstractService;
import liquid.transport.domain.RailContainer;
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
public class RailContainerServiceImpl extends AbstractService<RailContainer, RailContainerRepository>
        implements RailContainerService {
    @Override
    public void doSaveBefore(RailContainer entity) { }



    @Override
    public Collection<RailContainer> findByShipmentId(Long shipmentId) {
        return repository.findByShipmentId(shipmentId);
    }

    @Override
    public Iterable<RailContainer> findByReleasedAtToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
        return repository.findByReleasedAtGreaterThan(calendar.getTime());
    }

    @Override
    public Page<RailContainer> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
