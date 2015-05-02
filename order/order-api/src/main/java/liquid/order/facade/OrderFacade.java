package liquid.order.facade;

import liquid.order.domain.OrderEntity;
import liquid.order.model.Order;

/**
 * Created by Tao Ma on 5/2/15.
 */
public interface OrderFacade {
    void convert(OrderEntity orderEntity, Order order);
}
