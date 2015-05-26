package liquid.process.handler;

import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.process.domain.Task;
import liquid.process.model.Disty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * Created by Tao Ma on 4/15/15.
 */
@Component
public class FeedDistyPriceHandler extends AbstractTaskHandler {
    @Autowired
    private OrderService orderService;

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void init(Task task, Model model) {
        long orderId = taskService.getOrderIdByTaskId(task.getId());
        Order order = orderService.find(orderId);
        Disty disty = new Disty();
        disty.setDistyCny(order.getDistyCny());
        disty.setDistyUsd(order.getDistyUsd());
        model.addAttribute("disty", disty);
    }

    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) {}
}
