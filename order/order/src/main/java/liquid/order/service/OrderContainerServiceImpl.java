package liquid.order.service;

import liquid.order.domain.OrderContainer;
import liquid.order.repository.OrderContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mat on 4/29/16.
 */
@Service
public class OrderContainerServiceImpl implements OrderContainerService {

    @Autowired
    private OrderContainerRepository orderContainerRepository;

    @Override
    public OrderContainer find(Long id) {
        return orderContainerRepository.findOne(id);
    }
}
