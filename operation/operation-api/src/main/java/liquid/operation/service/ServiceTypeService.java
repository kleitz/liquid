package liquid.operation.service;

import liquid.operation.domain.ServiceType;

/**
 * Created by Tao Ma on 4/9/15.
 */
public interface ServiceTypeService {
    Iterable<ServiceType> findAll();

    ServiceType find(Long id);

    Iterable<ServiceType> findEnabled();

    ServiceType save(ServiceType serviceTypeEntity);
}
