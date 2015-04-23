package liquid.process.controller;

import liquid.core.model.Alert;
import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.process.handler.DefinitionKey;
import liquid.process.handler.TaskHandlerFactory;
import liquid.process.model.Disty;
import liquid.process.service.TaskService;
import liquid.core.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;

/**
 * Created by Tao Ma on 4/15/15.
 */
@Controller
public class FeedDistyPriceController extends AbstractTaskController {

    private static final Logger logger = LoggerFactory.getLogger(FeedDistyPriceController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TaskHandlerFactory factory;

    @RequestMapping(method = RequestMethod.POST, params = "definitionKey=" + DefinitionKey.feedDistyPrice)
    public String feed(@PathVariable String taskId,
                       @Valid @ModelAttribute("disty") Disty disty, BindingResult bindingResult,
                       Model model, RedirectAttributes redirectAttributes) {
        logger.debug("taskId: {}", taskId);

        if (bindingResult.hasErrors()) {
            return factory.locateHandler(DefinitionKey.feedDistyPrice).locateTemplate(DefinitionKey.feedDistyPrice);
        }

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        order.setDistyCny(disty.getDistyCny());
        order.setDistyUsd(disty.getDistyUsd());
        order.setUpdatedBy(SecurityContext.getInstance().getUsername());
        order.setUpdatedAt(new Date());
        orderService.save(order);

        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return computeRedirect(taskId);
    }
}
