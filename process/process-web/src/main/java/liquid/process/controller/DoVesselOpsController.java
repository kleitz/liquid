package liquid.process.controller;

import liquid.core.model.Alert;
import liquid.process.handler.DefinitionKey;
import liquid.process.model.VesselContainerListForm;
import liquid.process.service.TaskService;
import liquid.transport.domain.VesselContainer;
import liquid.transport.service.ShippingContainerService;
import liquid.transport.service.VesselContainerService;
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
 * Date: 10/12/13
 * Time: 4:28 PM
 */
@Controller
public class DoVesselOpsController extends AbstractTaskController {
    private static final Logger logger = LoggerFactory.getLogger(DoVesselOpsController.class);

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private VesselContainerService vesselContainerService;

    @RequestMapping(method = RequestMethod.POST, params = "definitionKey=" + DefinitionKey.doVesselOps)
    public String save(@PathVariable String taskId, VesselContainerListForm vesselContainerListForm,
                       Model model, RedirectAttributes redirectAttributes) {
        logger.debug("taskId: {}", taskId);
        logger.debug("vesselContainerListForm: {}", vesselContainerListForm);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        Iterable<VesselContainer> iterable = scService.initVesselContainers(orderId);
        for (VesselContainer oldOne : iterable) {
            for (VesselContainer newOne : vesselContainerListForm.getList()) {
                if (oldOne.getId() == newOne.getId()) {
                    oldOne.setBolNo(newOne.getBolNo());
                    oldOne.setSlot(newOne.getSlot());
                    oldOne.setEts(newOne.getEts());
                }
                continue;
            }
        }
        vesselContainerService.save(iterable);

        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return computeRedirect(taskId);
    }
}
