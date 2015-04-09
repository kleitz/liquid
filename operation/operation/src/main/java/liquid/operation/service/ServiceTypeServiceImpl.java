package liquid.operation.service;

import liquid.operation.domain.ServiceTypeEntity;
import liquid.operation.repository.ServiceTypeRepository;
import liquid.service.AbstractCachedService;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 4/26/14.
 */
@Service
public class ServiceTypeServiceImpl extends AbstractCachedService<ServiceTypeEntity, ServiceTypeRepository>
        implements ServiceTypeService {
    @Override
    public void doSaveBefore(ServiceTypeEntity entity) { }

    public Iterable<ServiceTypeEntity> findEnabled() {
        return repository.findByState(0);
    }
}
