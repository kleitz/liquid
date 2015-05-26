package liquid.process.handler;

import liquid.order.domain.Order;
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
@Component("CDCIHandler")
public class CDCIHandler extends AbstractTaskHandler {
    @Autowired
    private OrderService orderService;

    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) {

    }

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void init(Task task, Model model) {
        Order order = orderService.find(task.getOrderId());
        String verificationSheetSn = order.getVerificationSheetSn();
        VerificationSheetForm verificationSheetForm = new VerificationSheetForm();
        verificationSheetForm.setDefinitionKey(task.getDefinitionKey());
        verificationSheetForm.setSn(verificationSheetSn);
        verificationSheetForm.setOrderId(task.getOrderId());
        model.addAttribute("verificationSheetForm", verificationSheetForm);
    }
}
