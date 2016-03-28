package liquid.transport.facade;

import liquid.operation.domain.ServiceProvider;
import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.transport.domain.LegEntity;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.domain.SpaceBookingEntity;
import liquid.transport.domain.TransMode;
import liquid.transport.model.Booking;
import liquid.transport.model.BookingItem;
import liquid.transport.service.BookingService;
import liquid.transport.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by redbrick9 on 8/16/14.
 * FIXME - need to remove facade layer.
 */
@Service
public class BookingFacadeImpl implements BookingFacade {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BookingService bookingService;

    @Override
    public Booking computeBooking(Long orderId) {
        Booking booking = new Booking();

        Iterable<SpaceBookingEntity> bookingEntities = bookingService.findByOrderId(orderId);
        Order order = orderService.find(orderId);

        BookingItem bookingItem = new BookingItem();
        bookingItem.setContainerQuantity(order.getContainerQty());

        for (SpaceBookingEntity bookingEntity : bookingEntities) {
            if (bookingEntity.getOrder().getId().equals(order.getId())) {
                bookingItem.setId(bookingEntity.getId());
                bookingItem.setShipownerId(bookingEntity.getShipowner().getId());
                bookingItem.setShipownerName(bookingEntity.getShipowner().getName());
                bookingItem.setBookingNo(bookingEntity.getBookingNo());
            }
        }
        booking.getBookingItems().add(bookingItem);

        return booking;
    }

    @Override
    public void save(Long orderId, Booking booking) {
        List<SpaceBookingEntity> bookingEntities = new ArrayList<>();

        for (BookingItem bookingItem : booking.getBookingItems()) {
            SpaceBookingEntity bookingEntity = new SpaceBookingEntity();
            bookingEntity.setId(bookingItem.getId());
            bookingEntity.setOrder(Order.newInstance(Order.class, orderId));
            bookingEntity.setShipowner(ServiceProvider.newInstance(ServiceProvider.class, bookingItem.getShipownerId()));
            bookingEntity.setBookingNo(bookingItem.getBookingNo());
            bookingEntities.add(bookingEntity);
        }

        bookingService.save(bookingEntities);
    }
}
