package liquid.process.handler;

import liquid.accounting.domain.Charge;
import liquid.accounting.domain.ChargeWay;
import liquid.accounting.domain.Purchase;
import liquid.accounting.service.ChargeService;
import liquid.accounting.service.PurchaseService;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.ServiceProviderService;
import liquid.operation.service.ServiceSubtypeService;
import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.process.domain.Task;
import liquid.transport.domain.TransMode;
import liquid.transport.facade.BookingFacade;
import liquid.transport.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * Created by Tao Ma on 4/17/15.
 */
@Component
public class BookingShippingSpaceHandler extends AbstractTaskHandler {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    // FIXME - Need to use BookingService instead.
    @Autowired
    private BookingFacade bookingFacade;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void init(Task task, Model model) {
        Order order = orderService.find(task.getOrderId());

        Booking booking = bookingFacade.computeBooking(order.getId());
        model.addAttribute("booking", booking);

        Iterable<ServiceProvider> shipowners = serviceProviderService.findByType(3L);
        model.addAttribute("shipowners", shipowners);

        buildPurchase(task, model, order);
    }



    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) {

    }
}
