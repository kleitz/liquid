package liquid.order.repository;

import liquid.order.domain.OrderContainerChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by mat on 4/28/16.
 */
public interface OrderContainerChangeRepository extends CrudRepository<OrderContainerChange, Long>,
        JpaRepository<OrderContainerChange, Long> {

    List<OrderContainerChange> findByOrderId(Long orderId);
}
