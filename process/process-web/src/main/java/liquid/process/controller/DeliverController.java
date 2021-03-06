package liquid.process.controller;

import liquid.accounting.domain.Charge;
import liquid.accounting.domain.ChargeWay;
import liquid.accounting.service.ChargeService;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.ServiceSubtypeService;
import liquid.transport.domain.DeliveryContainerEntity;
import liquid.transport.domain.ShipmentEntity;
import liquid.transport.domain.TransMode;
import liquid.transport.service.DeliveryContainerService;
import liquid.transport.service.ShipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 *  
 * User: tao
 * Date: 10/16/13
 * Time: 10:28 PM
 */
@Controller
@RequestMapping("/task/{taskId}/delivery")
public class DeliverController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(DeliverController.class);

    @Autowired
    private DeliveryContainerService deliveryContainerService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        model.addAttribute("shipmentSet", shipmentSet);
        model.addAttribute("containers", deliveryContainerService.initDeliveryContainers(orderId));

        // for charges
        Iterable<ServiceSubtype> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);
        model.addAttribute("chargeWays", ChargeWay.values());
        model.addAttribute("transModes", TransMode.toMap());
        Iterable<Charge> charges = chargeService.findByTaskId(taskId);
        model.addAttribute("charges", charges);
        model.addAttribute("total", chargeService.total(charges));
        return "delivery/main";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.GET)
    public String initSave(@PathVariable String taskId,
                           @PathVariable long containerId,
                           Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        DeliveryContainerEntity container = deliveryContainerService.findDeliveryContainer(containerId);

        logger.debug("container: {}", container);
        model.addAttribute("container", container);
        return "delivery/edit";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.POST)
    public String save(@PathVariable String taskId,
                       @PathVariable long containerId,
                       @ModelAttribute("container") DeliveryContainerEntity formBean,
                       Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        deliveryContainerService.saveDeliveryContainer(containerId, formBean);

        return "redirect:/task/" + taskId + "/delivery";
    }
}
