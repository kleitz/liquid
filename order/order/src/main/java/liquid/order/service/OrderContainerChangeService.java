package liquid.order.service;

import liquid.core.security.SecurityContext;
import liquid.order.domain.OrderContainer;
import liquid.order.domain.OrderContainerChange;
import liquid.order.repository.OrderContainerChangeRepository;
import liquid.order.repository.OrderContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by mat on 4/28/16.
 */
@Service
public class OrderContainerChangeService {

    @Autowired
    private OrderContainerChangeRepository orderContainerChangeRepository;

    @Autowired
    private OrderContainerRepository orderContainerRepository;

    public List<OrderContainerChange> findByOrderId(Long orderId) {
        return orderContainerChangeRepository.findByOrderId(orderId);
    }

    @Transactional("transactionManager")
    public void addChange(OrderContainerChange orderContainerChange){
        OrderContainer orderContainer = orderContainerChange.getContainer();

        orderContainerChange.setOldValue(orderContainer.getBicCode());
        orderContainerChange.setChangedAt(new Date());
        orderContainerChange.setChangedBy(SecurityContext.getInstance().getUsername());
        orderContainerChangeRepository.save(orderContainerChange);

        orderContainer.setBicCode(orderContainerChange.getNewValue());
        orderContainerRepository.save(orderContainer);
    }
}
