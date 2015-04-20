package liquid.process.controller;

import liquid.core.model.Alert;
import liquid.operation.service.ServiceProviderService;
import liquid.process.model.RailContainerListForm;
import liquid.transport.domain.RailContainer;
import liquid.transport.service.RailContainerService;
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
 * This task has been removed from bpmn.
 * User: tao
 * Date: 10/19/13
 * Time: 3:38 PM
 */
@Controller
@RequestMapping("/task/{taskId}/rail_arrival")
public class RecordToaController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(RecordTodController.class);

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private RailContainerService railContainerService;

    @RequestMapping(method = RequestMethod.POST)
    public String save(@PathVariable String taskId, RailContainerListForm railContainerListForm,
                       Model model, RedirectAttributes redirectAttributes) {
        logger.debug("taskId: {}", taskId);
        logger.debug("railContainerListForm: {}", railContainerListForm);

        Long orderId = taskService.getOrderIdByTaskId(taskId);

        Iterable<RailContainer> iterable = scService.initializeRailContainers(orderId);
        for (RailContainer oldOne : iterable) {
            for (RailContainer newOne : railContainerListForm.getList()) {
                if (oldOne.getId() == newOne.getId()) {
                    oldOne.setAta(newOne.getAta());
                }
                continue;
            }
        }
        railContainerService.save(iterable);
        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return "redirect:/task/" + taskId + "/rail_arrival";
    }
}
