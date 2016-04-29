package liquid.order.service;

import liquid.order.domain.OrderContainer;

/**
 * Created by mat on 4/29/16.
 */
public interface OrderContainerService {
    OrderContainer find(Long id);
}
