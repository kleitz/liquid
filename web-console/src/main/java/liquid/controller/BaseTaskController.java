package liquid.controller;

import liquid.service.TaskServiceImpl;
import liquid.task.domain.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/4/13
 * Time: 5:42 PM
 */
@Component
public abstract class BaseTaskController extends BaseController {
    @Autowired
    protected TaskServiceImpl taskService;

    @ModelAttribute("task")
    public Task populateTask(@PathVariable String taskId) {
        return taskService.getTask(taskId);
    }

    @ModelAttribute("task_error")
    public String populateTaskError(@RequestParam(value = "task_error", required = false) String taskError) {
        return taskError;
    }
}
