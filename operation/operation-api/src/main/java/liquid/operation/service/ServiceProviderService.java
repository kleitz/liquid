package liquid.operation.service;

import liquid.operation.domain.ServiceProvider;
import liquid.service.CrudService;

import java.util.List;

/**
 * Created by Tao Ma on 4/2/15.
 */
public interface ServiceProviderService extends CrudService<ServiceProvider> {
    ServiceProvider find(Long id);

    Iterable<ServiceProvider> findByType(Long typeId);

    List<ServiceProvider> findByServiceSubtypeId(Long serviceSubtypeId);

    Iterable<ServiceProvider> findByQueryNameLike(String name);

    Iterable<ServiceProvider> findAll();
}
