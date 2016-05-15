package liquid.process.controller;

import liquid.order.domain.Order;
import liquid.order.domain.ServiceItem;
import liquid.order.service.OrderService;
import liquid.order.service.ServiceItemService;
import liquid.process.handler.DefinitionKey;
import liquid.process.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by mat on 5/14/16.
 */
@Controller
public class AdjustPriceController extends AbstractTaskController {
    private static final Logger logger = LoggerFactory.getLogger(AdjustPriceController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ServiceItemService serviceItemService;

    @RequestMapping(method = RequestMethod.POST, params = "definitionKey=" + DefinitionKey.adjustPrice)
    public String addServiceItem(@PathVariable String taskId, ServiceItem serviceItem) {
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        Order order = orderService.find(orderId);
        serviceItem.setStatus(0);
        order.getServiceItems().add(serviceItem);
        orderService.save(order);
        return computeRedirect(taskId);
    }

    @RequestMapping(method = RequestMethod.POST,
            params = {"definitionKey=" + DefinitionKey.adjustPrice, "voidServiceItem=voidServiceItem"})
    public String voidServiceItem(@PathVariable String taskId, Long id, String comment) {
        logger.debug("Service Item Id: {}; Comment: {}", id, comment);
        ServiceItem serviceItem = serviceItemService.find(id);
        serviceItem.setStatus(1);
        serviceItem.setComment(serviceItem.getComment() + ";" + comment);
        serviceItemService.save(serviceItem);
        return computeRedirect(taskId);
    }
}
