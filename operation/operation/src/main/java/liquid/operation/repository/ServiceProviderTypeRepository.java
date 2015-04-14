package liquid.operation.repository;

import liquid.operation.domain.ServiceProviderType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

/**
 * User: tao
 * Date: 10/25/13
 * Time: 7:33 PM
 */
public interface ServiceProviderTypeRepository extends CrudRepository<ServiceProviderType, Long> {
    @Override
    @Cacheable("serviceProviderTypeCache")
    Iterable<ServiceProviderType> findAll();
}
