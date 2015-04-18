package liquid.process.handler;

import liquid.process.service.TaskService;
import liquid.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
public abstract class AbstractTaskHandler implements TaskHandler {
    @Autowired
    protected TaskService taskService;

    @Override
    public String locateTemplate(String definitionKey) {
        return Constants.TASK_ROOT_DIR + "/" + definitionKey + "/init";
    }

    public abstract void preComplete(String taskId, Map<String, Object> variableMap);

    public void complete(String taskId) {
        Map<String, Object> variableMap = new HashMap<>();
        preComplete(taskId, variableMap);
        taskService.complete(taskId, SecurityContext.getInstance().getUsername(), variableMap);
    }
}
