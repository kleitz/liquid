package liquid.process.controller;

import liquid.core.controller.BaseController;
import liquid.process.handler.Constants;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Tao Ma on 4/16/15.
 */
@RequestMapping("/" + Constants.TASK_ROOT_PATH + "/{taskId}")
public abstract class AbstractTaskController extends BaseController {
    protected String computeRedirect(final String taskId) {
        return "redirect:/" + Constants.TASK_ROOT_PATH + "/" + taskId;
    }
}
