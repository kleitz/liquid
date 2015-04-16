package liquid.process.handler;

import liquid.process.domain.Task;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.domain.ShippingContainerEntity;
import liquid.transport.service.ShipmentService;
import liquid.transport.service.ShippingContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@Component
public class AllocateContainersHandler extends AbstractTaskHandler {

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ShippingContainerService shippingContainerService;

    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) {
        long orderId = taskService.getOrderIdByTaskId(taskId);
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        int shippingContainerQuantity = 0;

        // TODO: This is temp solution for dual-allocated containers.
        List<ShippingContainerEntity> shippingContainers = new ArrayList<ShippingContainerEntity>();
        for (ShipmentEntity shipment : shipmentSet) {
            Collection<ShippingContainerEntity> scs = shippingContainerService.findByShipmentId(shipment.getId());
            shippingContainerQuantity += scs.size();

            // TODO: This is temp solution for dual-allocated containers.
            for (int i = 0; i < shipment.getContainerQty() - shippingContainerQuantity; i++) {
                ShippingContainerEntity shippingContainer = new ShippingContainerEntity();
                shippingContainer.setShipment(shipment);
                shippingContainers.add(shippingContainer);
            }
        }
        // TODO: This is temp solution for dual-allocated containers.
        shippingContainerService.save(shippingContainers);
    }

    @Override
    public boolean isRedirect() {
        return true;
    }

    @Override
    public void init(Task task, Model model) {

    }
}