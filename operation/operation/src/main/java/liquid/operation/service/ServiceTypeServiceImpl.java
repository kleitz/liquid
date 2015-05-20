package liquid.operation.service;

import liquid.operation.domain.ServiceType;
import liquid.operation.repository.ServiceTypeRepository;
import liquid.core.service.AbstractCachedService;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 4/26/14.
 */
@Service
public class ServiceTypeServiceImpl extends AbstractCachedService<ServiceType, ServiceTypeRepository>
        implements ServiceTypeService {
    @Override
    public void doSaveBefore(ServiceType entity) { }

    public Iterable<ServiceType> findEnabled() {
        return repository.findByState(0);
    }
}
