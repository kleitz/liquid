package liquid.order.facade;

import liquid.core.validation.FormValidationResult;
import liquid.order.domain.OrderEntity;
import liquid.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Tao Ma on 5/2/15.
 */
public interface InternalOrderFacade extends OrderFacade {
    Page<Order> findByCreateUser(String username, Pageable pageable);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findByCustomerId(Long customerId, Pageable pageable);

    Order find(long id);

    Page<Order> findAll(final String orderNo, final String customerName, final String username, final Pageable pageable);

    Order initOrder();

    FormValidationResult validateCustomer(Long id, String name);

    FormValidationResult validateLocation(Long id, String name);

    FormValidationResult validateLocation(Long id, String name, Byte typeId);

    OrderEntity save(Order order);

    OrderEntity submit(Order order);

    Order duplicate(long id);
}
