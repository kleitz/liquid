package liquid.process.handler;

import liquid.process.domain.Task;
import liquid.process.model.VesselContainerListForm;
import liquid.transport.service.ShippingContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * Created by Tao Ma on 4/20/15.
 */
@Component
public class DoVesselOpsHandler extends AbstractTaskHandler {
    @Autowired
    private ShippingContainerService scService;

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void init(Task task, Model model) {
        model.addAttribute("containerListForm", new VesselContainerListForm(scService.initVesselContainers(task.getOrderId())));
        model.addAttribute("action", "/task/" + task.getId());
    }

    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) {}
}
