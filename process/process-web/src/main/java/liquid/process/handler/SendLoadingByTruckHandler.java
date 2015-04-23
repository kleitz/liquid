package liquid.process.handler;

import liquid.process.domain.Task;
import liquid.process.model.SendingTruckRole;
import liquid.user.domain.Role;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * Created by Tao Ma on 4/18/15.
 */
@Deprecated
@Component
public class SendLoadingByTruckHandler extends AbstractTaskHandler {
    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void init(Task task, Model model) {
        SendingTruckRole trucking = new SendingTruckRole();
        Object role = taskService.getVariable(task.getId(), "truckingRole");
        if (null != role) trucking.setRole(role.toString());

        model.addAttribute("roles", new Role[]{Role.SALES, Role.MARKETING});
        model.addAttribute("trucking", trucking);
    }

    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) { }
}
