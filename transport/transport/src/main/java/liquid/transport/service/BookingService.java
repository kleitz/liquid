package liquid.transport.service;

import liquid.core.service.AbstractService;
import liquid.transport.domain.SpaceBookingEntity;
import liquid.transport.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 8/15/14.
 */
@Service
public class BookingService extends AbstractService<SpaceBookingEntity, BookingRepository> {

    @Autowired
    private BookingRepository bookingRepository;

    public Iterable<SpaceBookingEntity> findByOrderId(Long orderId) {
        return bookingRepository.findByOrderId(orderId);
    }

    @Override
    public void doSaveBefore(SpaceBookingEntity entity) { }
}
