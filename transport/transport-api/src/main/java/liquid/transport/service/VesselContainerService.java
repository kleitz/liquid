package liquid.transport.service;

import liquid.transport.domain.VesselContainer;

/**
 * Created by Tao Ma on 4/20/15.
 */
public interface VesselContainerService {
    Iterable<VesselContainer> save(Iterable<VesselContainer> entities);
}
