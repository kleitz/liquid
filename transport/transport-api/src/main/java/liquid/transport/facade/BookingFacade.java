package liquid.transport.facade;

import liquid.transport.model.Booking;

/**
 * Created by Tao Ma on 4/17/15.
 */
public interface BookingFacade {

    Booking computeBooking(Long orderId);

    void save(Long orderId, Booking booking);
}
