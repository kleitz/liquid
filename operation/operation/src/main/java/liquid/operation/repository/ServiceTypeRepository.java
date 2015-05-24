package liquid.operation.repository;

import liquid.operation.domain.ServiceType;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 4/26/14.
 */
public interface ServiceTypeRepository extends CrudRepository<ServiceType, Long> {
    Iterable<ServiceType> findByState(int state);
}
