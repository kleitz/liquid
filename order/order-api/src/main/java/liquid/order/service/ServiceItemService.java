package liquid.order.service;

import liquid.operation.domain.ServiceProvider;
import liquid.order.domain.ServiceItem;

import java.util.List;

/**
 * Created by Tao Ma on 4/5/15.
 */
public interface ServiceItemService {
    List<ServiceProvider> findContainerOwners();

    ServiceItem save(ServiceItem serviceItem);

    ServiceItem find(Long id);
}
