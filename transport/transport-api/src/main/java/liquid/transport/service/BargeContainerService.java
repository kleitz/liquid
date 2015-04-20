package liquid.transport.service;

import liquid.transport.domain.BargeContainerEntity;

/**
 * Created by Tao Ma on 4/20/15.
 */
public interface BargeContainerService {
    Iterable<BargeContainerEntity> save(Iterable<BargeContainerEntity> entities);
}
