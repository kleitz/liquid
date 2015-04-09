package liquid.container.service;

import liquid.container.domain.ContainerSubtypeEntity;
import liquid.container.domain.ContainerType;
import liquid.container.repository.ContainerSubtypeRepository;
import liquid.service.AbstractCachedService;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 5/4/14.
 */
@Service
public class ContainerSubtypeServiceImpl extends AbstractCachedService<ContainerSubtypeEntity, ContainerSubtypeRepository>
        implements InternalContainerSubtypeService {
    @Override
    public void doSaveBefore(ContainerSubtypeEntity entity) { }

    @Override
    public Iterable<ContainerSubtypeEntity> findEnabled() {
        return repository.findByState(0);
    }

    @Override
    public Iterable<ContainerSubtypeEntity> findByContainerType(ContainerType containerType) {
        return repository.findByContainerType(containerType.getType());
    }
}
