package liquid.container.service;

import liquid.container.domain.ContainerSubtype;
import liquid.container.domain.ContainerType;
import liquid.container.repository.ContainerSubtypeRepository;
import liquid.core.service.AbstractCachedService;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 5/4/14.
 */
@Service
public class ContainerSubtypeServiceImpl extends AbstractCachedService<ContainerSubtype, ContainerSubtypeRepository>
        implements InternalContainerSubtypeService {
    @Override
    public void doSaveBefore(ContainerSubtype entity) { }

    @Override
    public Iterable<ContainerSubtype> findEnabled() {
        return repository.findByState(0);
    }

    @Override
    public Iterable<ContainerSubtype> findByContainerType(ContainerType containerType) {
        return repository.findByContainerType(containerType.getType());
    }
}
