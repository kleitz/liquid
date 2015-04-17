package liquid.process.controller;

import liquid.core.model.Alert;
import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.process.handler.DefinitionKey;
import liquid.process.service.TaskService;
import liquid.transport.facade.BookingFacade;
import liquid.transport.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by Tao Ma on 4/17/15.
 */
@Controller
public class BookingShippingSpaceController extends AbstractTaskController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TaskService taskService;

    // FIXME - using BookingService instead.
    @Autowired
    private BookingFacade bookingFacade;

    @RequestMapping(method = RequestMethod.POST, params = "definitionKey=" + DefinitionKey.bookingShippingSpace)
    public String booking(@PathVariable String taskId, Booking booking, Model model, RedirectAttributes redirectAttributes) {
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        bookingFacade.save(order.getId(), booking);

        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return computeRedirect(taskId);
    }
}
