package liquid.process.handler;

import liquid.operation.domain.ServiceSubtype;
import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.process.domain.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * Created by Tao Ma on 6/11/15.
 */
public class AbstractOrderPriceHandler extends AbstractTaskHandler {

    @Autowired
    private OrderService orderService;

    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) {}

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void init(Task task, Model model) {
        Order order = orderService.find(task.getOrderId());
        model.addAttribute("serviceItemList", order.getServiceItems());

        Iterable<ServiceSubtype> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);
    }

    @Override
    public String locateTemplate(String definitionKey) {
        return Constants.TASK_ROOT_DIR + "/order_price/init";
    }
}
