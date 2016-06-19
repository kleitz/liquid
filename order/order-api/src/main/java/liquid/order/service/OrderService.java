package liquid.order.service;

import liquid.order.domain.Order;
import liquid.order.domain.ServiceItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

    Page<Order> findAll(final Long id, final Long customerId, final String username, final Boolean isDiscarded,
                        final Pageable pageable);

    Page<Order> findByStatus(Integer status, Pageable pageable);

    Page<Order> findByCustomerId(Long customerId, String createdBy, Pageable pageable);

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByCustomerIdLessThanStatus(Long customerId, Integer status);

    Page<Order> findByOrderNoLike(String orderNo, Pageable pageable);

    Order saveOrder(Order order);

    Order addItem(Long id, ServiceItem item);

    Order voidItem(Long id, Long itemId);

    /**
     * Permanently delete a unsubmitted order.
     *
     * @param id
     */
    void delete(Long id);

    Order submitOrder(Order order);

    /**
     * Discard a submitted order.
     *
     * @param id
     * @return
     */
    Order discard(Long id);
}
