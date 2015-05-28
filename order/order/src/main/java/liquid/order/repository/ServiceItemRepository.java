package liquid.order.repository;

import liquid.order.domain.ServiceItem;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by tao on 12/25/13.
 */
public interface ServiceItemRepository extends CrudRepository<ServiceItem, Long> {}