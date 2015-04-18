package liquid.process.controller;

import liquid.accounting.domain.ChargeEntity;
import liquid.accounting.domain.ChargeWay;
import liquid.accounting.service.ChargeService;
import liquid.core.model.Alert;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.ServiceProviderService;
import liquid.operation.service.ServiceSubtypeService;
import liquid.process.handler.DefinitionKey;
import liquid.process.model.RailContainerListForm;
import liquid.transport.domain.RailContainerEntity;
import liquid.transport.domain.TransMode;
import liquid.transport.service.RailContainerService;
import liquid.transport.service.ShipmentService;
import liquid.transport.service.ShippingContainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * User: tao
 * Date: 10/19/13
 * Time: 12:08 AM
 */
@Controller
@RequestMapping("/task/{taskId}/rail_truck")
public class LoadOnYardController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(LoadOnYardController.class);

    private static final String TASK_PATH = "rail_truck";

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @Autowired
    private RailContainerService railContainerService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        model.addAttribute("containerListForm", new RailContainerListForm(scService.initializeRailContainers(orderId)));
        model.addAttribute("action", "/task/" + taskId + "/rail_truck");
        model.addAttribute("definitionKey", DefinitionKey.loadOnYard);
        // FIXME - this is bug, we need to use subtype instead.
        model.addAttribute("sps", serviceProviderService.findByType(4L));

        // for charges
        Iterable<ServiceSubtype> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);
        model.addAttribute("chargeWays", ChargeWay.values());
        model.addAttribute("transModes", TransMode.toMap());
        Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
        model.addAttribute("charges", charges);
        model.addAttribute("total", chargeService.total(charges));
        return "rail/main";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String save(@PathVariable String taskId, RailContainerListForm railContainerListForm,
                       Model model, RedirectAttributes redirectAttributes) {
        logger.debug("taskId: {}", taskId);
        logger.debug("railContainerListForm: {}", railContainerListForm);

        Long orderId = taskService.getOrderIdByTaskId(taskId);

        Iterable<RailContainerEntity> iterable = scService.initializeRailContainers(orderId);
        for (RailContainerEntity oldOne : iterable) {
            for (RailContainerEntity newOne : railContainerListForm.getList()) {
                if (oldOne.getId() == newOne.getId()) {
                    oldOne.setFleet(newOne.getFleet());
                    oldOne.setPlateNo(newOne.getPlateNo());
                    oldOne.setTrucker(newOne.getTrucker());
                    oldOne.setLoadingToc(newOne.getLoadingToc());
                }
                continue;
            }
        }
        railContainerService.save(iterable);

        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return "redirect:/task/" + taskId + "/rail_truck";
    }
}
