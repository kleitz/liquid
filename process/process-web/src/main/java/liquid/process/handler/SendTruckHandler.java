package liquid.process.handler;

import liquid.operation.service.ServiceProviderService;
import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.process.domain.Task;
import liquid.process.model.SendingTruckForm;
import liquid.transport.domain.Truck;
import liquid.transport.service.ShipmentService;
import liquid.transport.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Tao Ma on 4/16/15.
 */
@Component
public class SendTruckHandler extends AbstractTaskHandler {
    public static final String TASK_DEFINITION_KEY = "sendTruck";

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private TruckService truckService;

    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) {}

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void init(Task task, Model model) {
        Order order = orderService.find(task.getOrderId());

        SendingTruckForm sendingTruckForm = new SendingTruckForm();
        sendingTruckForm.setDefinitionKey(task.getDefinitionKey());
        sendingTruckForm.setOrderId(task.getOrderId());

        List<Truck> truckList = truckService.findByOrderId(order.getId());

        for (int i = truckList.size(); i < order.getContainerQty(); i++) {
            Truck truck = new Truck();
            truck.setPickingAt(new Date());
            truckList.add(truck);
        }
        sendingTruckForm.setTruckList(truckList);

        model.addAttribute("sendingTruckForm", sendingTruckForm);
        model.addAttribute("sps", serviceProviderService.findByType(4L));

        model.addAttribute("task", task);
    }
}
