package liquid.container.service;

import liquid.container.domain.ContainerSubtype;

/**
 * Created by Tao Ma on 4/9/15.
 */
public interface InternalContainerSubtypeService extends ContainerSubtypeService {
    Iterable<ContainerSubtype> findEnabled();

    ContainerSubtype save(ContainerSubtype containerSubtypeEntity);
}
