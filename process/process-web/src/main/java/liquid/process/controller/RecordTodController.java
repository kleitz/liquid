package liquid.process.controller;

import liquid.core.model.Alert;
import liquid.operation.service.ServiceProviderService;
import liquid.process.handler.DefinitionKey;
import liquid.process.model.RailContainerListForm;
import liquid.process.service.TaskService;
import liquid.transport.domain.RailContainer;
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
 * Time: 3:38 PM
 */
@Controller
public class RecordTodController extends AbstractTaskController {
    private static final Logger logger = LoggerFactory.getLogger(RecordTodController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private RailContainerService railContainerService;

    @RequestMapping(method = RequestMethod.POST, params = "definitionKey=" + DefinitionKey.recordTod)
    public String save(@PathVariable String taskId, RailContainerListForm railContainerListForm,
                       Model model, RedirectAttributes redirectAttributes) {
        logger.debug("taskId: {}", taskId);
        logger.debug("railContainerListForm: {}", railContainerListForm);

        Long orderId = taskService.getOrderIdByTaskId(taskId);

        Iterable<RailContainer> iterable = scService.initializeRailContainers(orderId);
        for (RailContainer oldOne : iterable) {
            for (RailContainer newOne : railContainerListForm.getList()) {
                if (oldOne.getId() == newOne.getId()) {
                    oldOne.setAts(newOne.getAts());
                }
                continue;
            }
        }
        railContainerService.save(iterable);
        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return computeRedirect(taskId);
    }
}
