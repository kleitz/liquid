package liquid.transport.service;

import liquid.transport.domain.BargeContainer;

/**
 * Created by Tao Ma on 4/20/15.
 */
public interface BargeContainerService {
    Iterable<BargeContainer> save(Iterable<BargeContainer> entities);
}
