package liquid.operation.service;

import liquid.operation.domain.ServiceProviderType;
import liquid.core.service.CrudService;

/**
 * Created by Tao Ma on 4/2/15.
 */
public interface ServiceProviderTypeService extends CrudService<ServiceProviderType> {
    Iterable<ServiceProviderType> findAll();
}
