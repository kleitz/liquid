package liquid.order.repository;

import liquid.order.domain.ServiceItemEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by tao on 12/25/13.
 */
public interface ServiceItemRepository extends CrudRepository<ServiceItemEntity, Long> {}