package liquid.process.handler;

import liquid.operation.service.ServiceProviderService;
import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.process.domain.Task;
import liquid.process.model.SendingTruckForm;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.domain.TruckEntity;
import liquid.transport.model.TruckForm;
import liquid.transport.service.ShipmentService;
import liquid.transport.service.TruckService;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.ArrayList;
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
    public void preComplete(String taskId, Map<String, Object> variableMap) {

    }

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

        Iterable<ShipmentEntity> shipmentEntities = shipmentService.findByOrderId(order.getId());
        List<TruckForm> truckList = new ArrayList<>();
        for (ShipmentEntity shipmentEntity : shipmentEntities) {
            Iterable<TruckEntity> truckEntityIterable = truckService.findByShipmentId(shipmentEntity.getId());
            List<TruckForm> truckFormListForShipment = new ArrayList<>();
            for (TruckEntity truckEntity : truckEntityIterable) {
                TruckForm truck = convert(truckEntity);
                truckFormListForShipment.add(truck);
            }

            for (int i = truckFormListForShipment.size(); i < shipmentEntity.getContainerQty(); i++) {
                TruckForm truck = new TruckForm();
                truck.setPickingAt(DateUtil.stringOf(new Date()));
                truck.setShipmentId(shipmentEntity.getId());
                truckFormListForShipment.add(truck);
            }
            truckList.addAll(truckFormListForShipment);
        }

        sendingTruckForm.setTruckList(truckList);

        model.addAttribute("sendingTruckForm", sendingTruckForm);
        model.addAttribute("sps", serviceProviderService.findByType(4L));

        model.addAttribute("task", task);
    }

    // FIXME - use Fomatter instead.
    private TruckForm convert(TruckEntity entity) {
        TruckForm truck = new TruckForm();
        truck.setId(entity.getId());
        truck.setShipmentId(entity.getShipment().getId());
        truck.setPickingAt(DateUtil.stringOf(entity.getPickingAt()));
        truck.setServiceProviderId(entity.getServiceProviderId());
        truck.setLicensePlate(entity.getLicensePlate());
        truck.setDriver(entity.getDriver());
        return truck;
    }
}
