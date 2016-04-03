package liquid.process.handler;

import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.process.domain.Task;
import liquid.process.model.VesselContainerListForm;
import liquid.transport.service.ShippingContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * Created by Tao Ma on 4/20/15.
 */
@Component
public class DoVesselOpsHandler extends AbstractTaskHandler {
    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private OrderService orderService;

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void init(Task task, Model model) {
        Order order = orderService.find(task.getOrderId());
        model.addAttribute("containerListForm", new VesselContainerListForm(scService.initVesselContainers(task.getOrderId())));
        model.addAttribute("action", "/task/" + task.getId());

        buildPurchase(task, model, order);
    }

    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) {}
}
