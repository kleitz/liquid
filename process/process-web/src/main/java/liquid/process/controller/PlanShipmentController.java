package liquid.process.controller;

import liquid.accounting.domain.Charge;
import liquid.accounting.domain.ChargeWay;
import liquid.accounting.service.ChargeService;
import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.process.handler.DefinitionKey;
import liquid.process.handler.TaskHandlerFactory;
import liquid.process.service.TaskService;
import liquid.transport.domain.*;
import liquid.transport.model.Shipment;
import liquid.transport.service.RouteService;
import liquid.transport.service.ShipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 4/16/15.
 */
@Controller
public class PlanShipmentController extends AbstractTaskController {
    private static final Logger logger = LoggerFactory.getLogger(PlanShipmentController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private TaskHandlerFactory factory;

    @RequestMapping(method = RequestMethod.POST, params = "definitionKey=" + DefinitionKey.planShipment)
    public String addShipment(@PathVariable String taskId, @Valid @ModelAttribute(value = "shipment") Shipment shipment,
                              BindingResult result, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipment: {}", shipment);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        Order order = orderService.find(orderId);

        int containerUsage = 0;
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        for (ShipmentEntity r : shipmentSet) {
            containerUsage += r.getContainerQty();
        }

        if (result.hasErrors()) {
            model.addAttribute("containerTotality", order.getContainerQty());

            model.addAttribute("shipmentSet", shipmentSet);
            model.addAttribute("transModes", TransMode.toMap());

            // charge table
            model.addAttribute("chargeWays", ChargeWay.values());
            Iterable<Charge> charges = chargeService.findByTaskId(taskId);
            model.addAttribute("charges", charges);
            model.addAttribute("total", chargeService.total(charges));
            return factory.locateHandler(DefinitionKey.planShipment).locateTemplate(DefinitionKey.planShipment);
        } else if (shipment.getContainerQuantity() > (order.getContainerQty() - containerUsage)) {
            addFieldError(result, "shipment", "containerQty", shipment.getContainerQuantity(), (order.getContainerQty() - containerUsage));

            model.addAttribute("containerTotality", order.getContainerQty());

            model.addAttribute("shipmentSet", shipmentSet);
            model.addAttribute("transModes", TransMode.toMap());

            // charge table
            model.addAttribute("chargeWays", ChargeWay.values());
            Iterable<Charge> charges = chargeService.findByTaskId(taskId);
            model.addAttribute("charges", charges);
            model.addAttribute("total", chargeService.total(charges));
            return factory.locateHandler(DefinitionKey.planShipment).locateTemplate(DefinitionKey.planShipment);
        } else {
            ShipmentEntity shipmentEntityntity = new ShipmentEntity();
            shipmentEntityntity.setOrder(order);
            shipmentEntityntity.setContainerQty(shipment.getContainerQuantity());
            shipmentEntityntity.setTaskId(taskId);

            if (null != shipment.getRouteId() && shipment.getRouteId() > 0L) {
                RouteEntity route = routeService.find(shipment.getRouteId());
                List<PathEntity> paths = route.getPaths();
                List<LegEntity> legs = new ArrayList<>(paths.size());
                for (PathEntity path : paths) {
                    LegEntity leg = new LegEntity();
                    leg.setShipment(shipmentEntityntity);
                    leg.setTransMode(path.getTransportMode());
                    leg.setSrcLoc(path.getFrom());
                    leg.setDstLoc(path.getTo());
                    legs.add(leg);
                }
                shipmentEntityntity.setLegs(legs);
            }

            shipmentService.save(shipmentEntityntity);
            return computeRedirect(taskId);
        }
    }
}
