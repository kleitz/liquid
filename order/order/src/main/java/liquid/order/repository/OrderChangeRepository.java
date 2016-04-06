package liquid.order.repository;

import liquid.order.domain.OrderChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mat on 4/5/16.
 */
public interface OrderChangeRepository extends CrudRepository<OrderChange, Long>, JpaRepository<OrderChange, Long> {
}
