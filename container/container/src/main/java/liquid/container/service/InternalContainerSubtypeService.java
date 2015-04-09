package liquid.container.service;

import liquid.container.domain.ContainerSubtypeEntity;

/**
 * Created by Tao Ma on 4/9/15.
 */
public interface InternalContainerSubtypeService extends ContainerSubtypeService {
    Iterable<ContainerSubtypeEntity> findEnabled();

    ContainerSubtypeEntity save(ContainerSubtypeEntity containerSubtypeEntity);
}
