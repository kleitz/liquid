package liquid.transport.repository;

import liquid.transport.domain.SpaceBookingEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 8/15/14.
 */
public interface BookingRepository extends CrudRepository<SpaceBookingEntity, Long> {
    Iterable<SpaceBookingEntity> findByOrderId(Long orderId);
}
