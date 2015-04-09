package liquid.container.service;

import liquid.container.domain.ContainerSubtypeEntity;
import liquid.container.domain.ContainerType;

/**
 * Created by Tao Ma on 4/9/15.
 */
public interface ContainerSubtypeService {
    ContainerSubtypeEntity find(Long id);

    Iterable<ContainerSubtypeEntity> findByContainerType(ContainerType containerType);
}
