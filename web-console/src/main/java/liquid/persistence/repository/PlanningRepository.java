package liquid.persistence.repository;

import liquid.persistence.domain.Order;
import liquid.persistence.domain.Planning;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 12:07 PM
 */
public interface PlanningRepository extends CrudRepository<Planning, String> {
    Planning findByOrder(Order order);
}
