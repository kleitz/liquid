package liquid.transport.service;

import liquid.transport.domain.RailContainerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * Created by Tao Ma on 4/9/15.
 */
public interface RailContainerService {
    // FIXME - Page should not occur here.
    Page<RailContainerEntity> findAll(Pageable pageable);

    // FIXME - The method should be internal.
    Collection<RailContainerEntity> findByShipmentId(Long shipmentId);

    RailContainerEntity save(RailContainerEntity railContainerEntity);

    Iterable<RailContainerEntity> findByReleasedAtToday();
}
