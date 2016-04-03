package liquid.process.handler;

import liquid.accounting.domain.Charge;
import liquid.accounting.domain.ChargeWay;
import liquid.accounting.service.ChargeService;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.ServiceProviderService;
import liquid.operation.service.ServiceSubtypeService;
import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.process.domain.Task;
import liquid.process.model.RailContainerListForm;
import liquid.process.service.TaskService;
import liquid.transport.domain.RailContainer;
import liquid.transport.domain.TransMode;
import liquid.transport.service.ShippingContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.Map;

/**
 * Created by Tao Ma on 4/18/15.
 */
public abstract class AbstractRailContainerHandler extends AbstractTaskHandler {

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

        Iterable<RailContainer> containers = scService.initializeRailContainers(task.getOrderId());
        for (RailContainer container : containers) {
            if (null == container.getEts()) container.setEts(new Date());
            if (null == container.getAts()) container.setAts(new Date());
            if (null == container.getAta()) container.setAta(new Date());
            if (null == container.getLoadingToc()) container.setLoadingToc(new Date());
            if (null == container.getStationToa()) container.setStationToa(new Date());
            if (null == container.getReleasedAt()) container.setReleasedAt(new Date());
        }
        model.addAttribute("containerListForm", new RailContainerListForm(containers));
        model.addAttribute("action", "/task/" + task.getId());

        buildPurchase(task, model, order);
    }

    @Override
    public String locateTemplate(String definitionKey) {
        return Constants.TASK_ROOT_DIR + "/rail_container/init";
    }

    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) { }
}
