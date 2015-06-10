package liquid.order.service;

import liquid.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Tao Ma on 4/4/15.
 */
public interface OrderService {
    Order complete(Long orderId);

    Order find(Long id);

    Order save(Order order);

    // FIXME - The following methods for the internal use.
    String computeOrderNo(String role, String serviceCode);

    Page<Order> findByCreateUser(String username, Pageable pageable);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findAll(final Long id, final Long customerId, final String username, final Pageable pageable);

    Page<Order> findByStatus(Integer status, Pageable pageable);

    Page<Order> findByCustomerId(Long customerId, String createdBy, Pageable pageable);

    Page<Order> findByOrderNoLike(String orderNo, Pageable pageable);

    Order saveOrder(Order order);

    Order submitOrder(Order order);
}
