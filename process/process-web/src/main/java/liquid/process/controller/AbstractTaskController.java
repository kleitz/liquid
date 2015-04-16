package liquid.process.controller;

import liquid.core.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Tao Ma on 4/16/15.
 */
@RequestMapping("/" + AbstractTaskController.TASK_ROOT_PATH + "/{taskId}")
public abstract class AbstractTaskController extends BaseController {
    public final static String TASK_ROOT_PATH = "task";

    protected String locateTemplate(final String definitionKey) {return TASK_ROOT_PATH + "/" + definitionKey + "/init";}

    protected String computeRedirect(final String taskId) {
        return "redirect:/" + TASK_ROOT_PATH + "/" + taskId;
    }
}
