package liquid.order.service;

import liquid.core.security.SecurityContext;
import liquid.order.domain.Order;
import liquid.order.domain.OrderChange;
import liquid.order.repository.OrderChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * Created by mat on 4/21/16.
 */
@Service
public class OrderChangeService {

    @Autowired
    private OrderChangeRepository orderChangeRepository;

    @Autowired
    private OrderService orderService;

    public List<OrderChange> findByOrderId(Long orderId) {
        return orderChangeRepository.findByOrderId(orderId);
    }

    @Transactional("transactionManager")
    public void addChange(OrderChange orderChange)  {
        Order order = orderService.find(orderChange.getOrder().getId());

        try {
            orderChange.setChangedAt(new Date());
            orderChange.setChangedBy(SecurityContext.getInstance().getUsername());
            Method get = Order.class.getMethod("get" + capitalize(orderChange.getItem()));
            Object old = get.invoke(order);
            orderChange.setOldValue(String.valueOf(old));
            Method set = Order.class.getMethod("set" + capitalize(orderChange.getItem()), get.getReturnType());
            set.invoke(order, Integer.valueOf(orderChange.getNewValue()));
            orderService.save(order);
            orderChangeRepository.save(orderChange);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
