package liquid.process.controller;

import liquid.process.handler.DefinitionKey;
import liquid.process.model.SendingTruckRole;
import liquid.process.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by Tao Ma on 4/18/15.
 * FIXME - Will delete it after GA.
 */
@Deprecated
@Controller
public class SendLoadingByTruckController extends AbstractTaskController {
    private static final Logger logger = LoggerFactory.getLogger(SendLoadingByTruckController.class);

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/sending", method = RequestMethod.POST, params = "definitionKey=" + DefinitionKey.sendLoadingByTruck)
    public String trucking(@PathVariable String taskId, @Valid @ModelAttribute("trucking") SendingTruckRole trucking) {
        logger.debug("taskId: {}", taskId);
        logger.debug("trucking: {}", trucking);

        taskService.setVariable(taskId, "truckingRole", trucking.getRole());

        return computeRedirect(taskId);
    }
}
