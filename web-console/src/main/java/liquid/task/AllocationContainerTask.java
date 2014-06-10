package liquid.task;

import liquid.persistence.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@DefinitionKey("allocateContainers")
@Service
public class AllocationContainerTask extends AbstractTaskProxy {

    @Override
    public void doBeforeComplete(String taskId, Map<String, Object> variableMap) {
        long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        Planning planning = planningService.findByOrder(order);
        Collection<RouteEntity> routes = routeService.findByPlanning(planning);
        int allocatedContainerQty = 0;

        // TODO: This is temp solution for dual-allocated containers.
        List<ShippingContainerEntity> shippingContainers = new ArrayList<ShippingContainerEntity>();
        for (RouteEntity route : routes) {
            Collection<ShippingContainerEntity> scs = shippingContainerService.findByRoute(route);
            allocatedContainerQty += scs.size();

            // TODO: This is temp solution for dual-allocated containers.
            for (int i = 0; i < route.getContainerQty() - scs.size(); i++) {
                ShippingContainerEntity shippingContainer = new ShippingContainerEntity();
                shippingContainer.setRoute(route);
                shippingContainers.add(shippingContainer);
            }
        }
        // TODO: This is temp solution for dual-allocated containers.
        shippingContainerService.save(shippingContainers);
//        if (allocatedContainerQty != order.getContainerQty()) {
//            throw new NotCompletedException("container.allocation.is.not.completed");
//        }
    }
}