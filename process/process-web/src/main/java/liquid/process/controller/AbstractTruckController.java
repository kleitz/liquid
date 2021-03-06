package liquid.process.controller;

import liquid.core.model.Alert;
import liquid.process.model.RailContainerListForm;
import liquid.process.service.TaskService;
import liquid.transport.domain.RailContainer;
import liquid.transport.service.RailContainerService;
import liquid.transport.service.ShippingContainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by Tao Ma on 4/20/15.
 */
public class AbstractTruckController extends AbstractTaskController {
    private static final Logger logger = LoggerFactory.getLogger(AbstractTruckController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private RailContainerService railContainerService;

    public String save(@PathVariable String taskId, RailContainerListForm railContainerListForm,
                       Model model, RedirectAttributes redirectAttributes) {
        logger.debug("TaskId: {}; RailContainerListForm: {}", taskId, railContainerListForm);

        Long orderId = taskService.getOrderIdByTaskId(taskId);

        Iterable<RailContainer> iterable = scService.initializeRailContainers(orderId);
        for (RailContainer oldOne : iterable) {
            for (RailContainer newOne : railContainerListForm.getList()) {
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
        return computeRedirect(taskId);
    }
}
