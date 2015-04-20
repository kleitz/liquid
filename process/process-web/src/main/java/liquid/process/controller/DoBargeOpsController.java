package liquid.process.controller;

import liquid.core.model.Alert;
import liquid.process.handler.DefinitionKey;
import liquid.process.model.BargeContainerListForm;
import liquid.process.service.TaskService;
import liquid.transport.domain.BargeContainerEntity;
import liquid.transport.service.BargeContainerService;
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
 * Date: 10/12/13
 * Time: 3:33 PM
 */
@Controller
public class DoBargeOpsController extends AbstractTaskController {
    private static final Logger logger = LoggerFactory.getLogger(DoBargeOpsController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private BargeContainerService bargeContainerService;

    @RequestMapping(method = RequestMethod.POST, params = "definitionKey=" + DefinitionKey.doBargeOps)
    public String save(@PathVariable String taskId, BargeContainerListForm railContainerListForm,
                       Model model, RedirectAttributes redirectAttributes) {
        logger.debug("taskId: {}", taskId);
        logger.debug("railContainerListForm: {}", railContainerListForm);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        Iterable<BargeContainerEntity> iterable = scService.initBargeContainers(orderId);
        for (BargeContainerEntity oldOne : iterable) {
            for (BargeContainerEntity newOne : railContainerListForm.getList()) {
                if (oldOne.getId() == newOne.getId()) {
                    oldOne.setBolNo(newOne.getBolNo());
                    oldOne.setSlot(newOne.getSlot());
                    oldOne.setEts(newOne.getEts());
                }
                continue;
            }
        }
        bargeContainerService.save(iterable);

        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return computeRedirect(taskId);
    }
}
