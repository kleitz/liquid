package liquid.transport.service;

import liquid.transport.domain.RailContainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * Created by Tao Ma on 4/9/15.
 */
public interface RailContainerService {
    // FIXME - Page should not occur here.
    Page<RailContainer> findAll(Pageable pageable);

    // FIXME - The method should be internal.
    Collection<RailContainer> findByShipmentId(Long shipmentId);

    RailContainer save(RailContainer railContainer);

    Iterable<RailContainer> save(Iterable<RailContainer> entities);

    Iterable<RailContainer> findByReleasedAtToday();
}
