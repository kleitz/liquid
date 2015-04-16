package liquid.process.handler;

import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.process.domain.Task;
import liquid.process.domain.VerificationSheetForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * FIXME - Need to change name by convention.
 * Created by Tao Ma on 4/16/15.
 */
@Component(CDCIHandler.TASK_DEFINITION_KEY + "Handler")
public class CDCIHandler extends AbstractTaskHandler {
    public static final String TASK_DEFINITION_KEY = "CDCI";

    @Autowired
    private OrderService orderService;

    @Override
    public void doBeforeComplete(String taskId, Map<String, Object> variableMap) {

    }

    @Override
    public String getDefinitionKey() {
        return TASK_DEFINITION_KEY;
    }

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void init(Task task, Model model) {
        OrderEntity order = orderService.find(task.getOrderId());
        String verificationSheetSn = order.getVerificationSheetSn();
        VerificationSheetForm verificationSheetForm = new VerificationSheetForm();
        verificationSheetForm.setDefinitionKey(task.getDefinitionKey());
        verificationSheetForm.setSn(verificationSheetSn);
        verificationSheetForm.setOrderId(task.getOrderId());
        model.addAttribute("verificationSheetForm", verificationSheetForm);
    }

    @Override
    public void claim(Task task) {

    }
}
