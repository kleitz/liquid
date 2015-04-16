package liquid.process.controller;

import liquid.accounting.domain.ChargeEntity;
import liquid.accounting.domain.ChargeWay;
import liquid.accounting.facade.ReceivableFacade;
import liquid.accounting.model.Earning;
import liquid.accounting.service.ChargeService;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.ServiceProviderService;
import liquid.operation.service.ServiceSubtypeService;
import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.process.NotCompletedException;
import liquid.process.domain.Task;
import liquid.process.handler.TaskHandler;
import liquid.process.handler.TaskHandlerFactory;
import liquid.process.service.TaskService;
import liquid.security.SecurityContext;
import liquid.transport.service.RouteService;
import liquid.transport.service.ShipmentService;
import liquid.transport.service.TruckService;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * User: tao
 * Date: 9/26/13
 * Time: 11:25 PM
 */
@Controller
@RequestMapping("/task/{taskId}")
public class TaskController extends AbstractTaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskHandlerFactory factory;

    @Autowired
    private TaskService taskService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ReceivableFacade receivableFacade;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private TruckService truckService;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);
        Task task = taskService.getTask(taskId);
        logger.debug("task: {}", task);

        model.addAttribute("task", task);

        TaskHandler handler = factory.locateHandler(task.getDefinitionKey());
        // FIXME - This is temporary solution for refactor.
        if (handler.isRedirect()) {
            return "redirect:" + taskService.computeTaskMainPath(taskId);
        }
        handler.init(task, model);
        return locateTemplate(task.getDefinitionKey());
    }

    @RequestMapping(method = RequestMethod.POST, params = "claim")
    public String claim(@PathVariable String taskId, Model model, RedirectAttributes redirectAttributes) {
        logger.debug("taskId: {}", taskId);
        try {
            taskService.claim(taskId, SecurityContext.getInstance().getUsername());
        } catch (ActivitiTaskAlreadyClaimedException e) {
            // FIXME
            model.addAttribute("message", "task.claimed.by.someone.else");
            return "error/error";
        }
        return "redirect:/task/" + taskId;
    }

    @RequestMapping(method = RequestMethod.POST, params = "complete")
    public String complete(@PathVariable String taskId,
                           @RequestHeader(value = "referer") String referer,
                           Model model) {
        logger.debug("taskId: {}", taskId);

        try {
            taskService.complete(taskId);
        } catch (NotCompletedException e) {
            return "redirect:" + referer;
        } catch (Exception e) {
            logger.error(String.format("Complete taskId '%s' and referer '%s'.", taskId, referer), e);
            return "redirect:" + referer;
        }

        return "redirect:/task";
    }

    /**
     * TODO: Move to another controller.
     *
     * @param taskId
     * @param model
     * @return
     */
    @RequestMapping(value = "/common", method = RequestMethod.GET)
    public String toCommon(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);
        Task task = taskService.getTask(taskId);
        model.addAttribute("task", task);
        return "task/common";
    }

    @RequestMapping(value = "/check_amount", method = RequestMethod.GET)
    public String checkAmount(@PathVariable String taskId, Model model) {
        long orderId = taskService.getOrderIdByTaskId(taskId);
        Task task = taskService.getTask(taskId);
        model.addAttribute("task", task);

        if ("ROLE_COMMERCE".equals(SecurityContext.getInstance().getRole())) {
            Iterable<ChargeEntity> charges = chargeService.findByOrderId(orderId);
            model.addAttribute("charges", charges);
        } else {
            Iterable<ChargeEntity> charges = chargeService.findByOrderIdAndCreateRole(orderId,
                    SecurityContext.getInstance().getRole());
            model.addAttribute("charges", charges);
        }

        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<ServiceSubtype> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);
        return "charge/list";
    }

    @RequestMapping(value = "/settlement", method = RequestMethod.GET)
    public String settle(@PathVariable String taskId, Model model) {
        Task task = taskService.getTask(taskId);
        model.addAttribute("task", task);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        Iterable<ChargeEntity> charges = chargeService.findByOrderId(order.getId());
        model.addAttribute("charges", charges);

        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<ServiceSubtype> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);

        Earning earning = receivableFacade.calculateEarning(order.getId());
        model.addAttribute("earning", earning);
        return "charge/settlement";
    }
}
