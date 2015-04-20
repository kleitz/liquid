package liquid.process.handler;

import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.process.NotCompletedException;
import liquid.process.domain.Task;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.domain.ShippingContainerEntity;
import liquid.transport.service.ShipmentService;
import liquid.transport.service.ShippingContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.Map;

/**
 * Created by redbrick9 on 7/22/14.
 */
@Component
public class FeedContainerNoHandler extends AbstractTaskHandler {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ShippingContainerService shippingContainerService;

    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) {
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);

        int allocatedContainerQuantity = 0;

        for (ShipmentEntity shipment : shipmentSet) {
            Collection<ShippingContainerEntity> scs = shippingContainerService.findByShipmentId(shipment.getId());
            for (ShippingContainerEntity shippingContainer : scs) {
                if (null != shippingContainer.getContainer() || null != shippingContainer.getBicCode()) {
                    allocatedContainerQuantity++;
                }
            }
        }

        if (allocatedContainerQuantity != order.getContainerQty()) {
            throw new NotCompletedException("container.allocation.is.not.completed");
        }
    }

    @Override
    public boolean isRedirect() {
        return true;
    }

    @Override
    public void init(Task task, Model model) {

    }
}
