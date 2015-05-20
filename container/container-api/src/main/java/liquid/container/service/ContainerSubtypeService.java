package liquid.container.service;

import liquid.container.domain.ContainerSubtype;
import liquid.container.domain.ContainerType;

/**
 * Created by Tao Ma on 4/9/15.
 */
public interface ContainerSubtypeService {
    Iterable<ContainerSubtype> findAll();

    ContainerSubtype find(Long id);

    Iterable<ContainerSubtype> findByContainerType(ContainerType containerType);
}
