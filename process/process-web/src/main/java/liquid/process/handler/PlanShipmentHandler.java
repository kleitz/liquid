package liquid.process.handler;

import liquid.accounting.domain.Charge;
import liquid.accounting.domain.ChargeWay;
import liquid.accounting.service.ChargeService;
import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.process.domain.Task;
import liquid.transport.domain.LegEntity;
import liquid.transport.domain.RouteEntity;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.domain.TransMode;
import liquid.transport.model.Shipment;
import liquid.transport.service.RouteService;
import liquid.transport.service.ShipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@Component
public class PlanShipmentHandler extends AbstractTaskHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlanShipmentHandler.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ChargeService chargeService;

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void init(Task task, Model model) {
        Order order = orderService.find(task.getOrderId());

        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(task.getOrderId());

        int containerUsage = 0;
        for (ShipmentEntity shipment : shipmentSet) {
            containerUsage += shipment.getContainerQty();
        }

        Shipment shipment = new Shipment();
        // set remaining container quantity as the default value for the next shipment planning.
        shipment.setContainerQuantity(order.getContainerQty() - containerUsage);

        // shipment planning bar
        model.addAttribute("shipment", shipment);
        model.addAttribute("containerTotality", order.getContainerQty());

        List<RouteEntity> routes = routeService.find(order.getSource().getId(), order.getDestination().getId());
        routes.add(RouteEntity.newInstance(RouteEntity.class, 0L));
        model.addAttribute("routes", routes);

        // shipment table
        model.addAttribute("shipmentSet", shipmentSet);
        model.addAttribute("transModes", TransMode.toMap());

        // charge table
        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<Charge> charges = chargeService.findByTaskId(task.getId());
        model.addAttribute("charges", charges);
        model.addAttribute("total", chargeService.total(charges));
    }

    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) {
        Map<String, Object> transTypes = getTransTypes(taskId);
        variableMap.putAll(transTypes);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        boolean hasWaterTransport = false;
        for (ShipmentEntity shipment : shipmentSet) {
            Collection<LegEntity> legs = shipment.getLegs();
            for (LegEntity leg : legs) {
                switch (TransMode.valueOf(leg.getTransMode())) {
                    case BARGE:
                    case VESSEL:
                        hasWaterTransport = true;
                        break;
                    default:
                        break;
                }
                if (hasWaterTransport) break;
            }
            if (hasWaterTransport) break;
        }
        variableMap.put("hasWaterTransport", hasWaterTransport);
    }

    @Transactional("transactionManager")
    public Map<String, Object> getTransTypes(String taskId) {
        Map<String, Object> transTypes = new HashMap<String, Object>();
        transTypes.put("hasRailway", false);
        transTypes.put("hasBarge", false);
        transTypes.put("hasVessel", false);

        Long orderId = taskService.getOrderIdByTaskId(taskId);

        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        for (ShipmentEntity shipment : shipmentSet) {
            Collection<LegEntity> legs = shipment.getLegs();
            for (LegEntity leg : legs) {
                TransMode mode = TransMode.valueOf(leg.getTransMode());
                switch (mode) {
                    case RAIL:
                        transTypes.put("hasRailway", true);
                        break;
                    case BARGE:
                        transTypes.put("hasBarge", true);
                        break;
                    case VESSEL:
                        transTypes.put("hasVessel", true);
                        break;
                    default:
                        logger.warn("{} transportation mode is illegal.");
                        break;
                }
            }
        }

        logger.debug("The order has the transportation {}.", transTypes);
        return transTypes;
    }
}
