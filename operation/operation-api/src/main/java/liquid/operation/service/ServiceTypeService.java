package liquid.operation.service;

import liquid.operation.domain.ServiceTypeEntity;

/**
 * Created by Tao Ma on 4/9/15.
 */
public interface ServiceTypeService {
    Iterable<ServiceTypeEntity> findAll();

    ServiceTypeEntity find(Long id);

    Iterable<ServiceTypeEntity> findEnabled();

    ServiceTypeEntity save(ServiceTypeEntity serviceTypeEntity);
}
