package liquid.process.controller;

import liquid.core.model.Alert;
import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.process.domain.Task;
import liquid.process.domain.VerificationSheetForm;
import liquid.process.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Created by Tao Ma on 4/16/15.
 */
public class CDCIController extends AbstractTaskController {
    private static final Logger logger = LoggerFactory.getLogger(CDCIController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.POST, params = "definitionKey=CDCI")
    public String fillIn(@PathVariable String taskId,
                         @Valid @ModelAttribute VerificationSheetForm verificationSheetForm, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes) {
        logger.debug("taskId: {}", taskId);
        Task task = taskService.getTask(taskId);
        logger.debug("task: {}", task);

        if (bindingResult.hasErrors()) {
            model.addAttribute("verificationSheetForm", verificationSheetForm);
            model.addAttribute("task", task);
            return "order/verification_sheet_sn";
        }

        OrderEntity order = orderService.find(verificationSheetForm.getOrderId());
        order.setVerificationSheetSn(verificationSheetForm.getSn());
        orderService.save(order);

        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return computeRedirect(taskId);
    }
}
