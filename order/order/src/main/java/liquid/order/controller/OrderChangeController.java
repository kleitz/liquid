package liquid.order.controller;

import liquid.order.domain.Order;
import liquid.order.domain.OrderChange;
import liquid.order.service.OrderService;
import liquid.process.service.BusinessKey;
import liquid.process.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by mat on 4/16/16.
 */
@Controller
@RequestMapping("/orders/{id}/changes")
public class OrderChangeController {
    private static final Logger logger = LoggerFactory.getLogger(OrderChangeController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProcessService processService;

    @RequestMapping(method = RequestMethod.POST)
    public String add(@PathVariable Long id, OrderChange change, @RequestHeader(value = "referer") String referer) {
        logger.debug("order id: {}; change: {}; referer: {}.",id, change, referer);
        Order order = orderService.find(id);
        processService.messageEventReceived(BusinessKey.encode(order.getId(), order.getOrderNo()));
        return "redirect:" + referer;
    }
}
