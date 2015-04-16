package liquid.process.handler;

import liquid.order.domain.OrderEntity;
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
    public static final String TASK_DEFINITION_KEY = "feedDistyPrice";

    @Autowired
    private OrderService orderService;

    @Override
    public void doBeforeComplete(String taskId, Map<String, Object> variableMap) {

    }

    @Override
    public final String getDefinitionKey() {
        return TASK_DEFINITION_KEY;
    }

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void init(Task task, Model model) {
        long orderId = taskService.getOrderIdByTaskId(task.getId());
        OrderEntity order = orderService.find(orderId);
        Disty disty = new Disty();
        disty.setDistyCny(order.getDistyCny());
        disty.setDistyUsd(order.getDistyUsd());
        model.addAttribute("disty", disty);
    }

    @Override
    public void claim(Task task) {

    }
}
