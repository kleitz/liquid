package liquid.process.handler;

import liquid.accounting.domain.ChargeWay;
import liquid.accounting.domain.Purchase;
import liquid.accounting.service.PurchaseService;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.ServiceProviderService;
import liquid.operation.service.ServiceSubtypeService;
import liquid.operation.service.TaxRateService;
import liquid.order.domain.Order;
import liquid.process.domain.Task;
import liquid.process.service.TaskService;
import liquid.core.security.SecurityContext;
import liquid.transport.domain.TransMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
public abstract class AbstractTaskHandler implements TaskHandler {
    @Autowired
    protected TaskService taskService;

    @Autowired
    protected PurchaseService purchaseService;

    @Autowired
    protected ServiceProviderService serviceProviderService;

    @Autowired
    protected ServiceSubtypeService serviceSubtypeService;

    @Autowired
    protected TaxRateService taxRateService;

    private String definitionKey;

    public String getDefinitionKey() {
        return definitionKey;
    }

    public void setDefinitionKey(String definitionKey) {
        this.definitionKey = definitionKey;
    }

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

    protected void buildPurchase(Task task, Model model, Order order) {
        model.addAttribute("chargeWays", ChargeWay.values());
        List<Purchase> purchases = purchaseService.findByOrderId(order.getId());
        model.addAttribute("purchases", purchases);

        Purchase purchase = new Purchase();
        purchase.setOrder(order);
        purchase.setTaskId(task.getId());
        purchase.setTransportMode(TransMode.VESSEL.getType());
        model.addAttribute("purchase", purchase);

        model.addAttribute("containerQuantity", order.getContainerQty());

        model.addAttribute("transportModeOptions", TransMode.values());
        model.addAttribute("transModes", TransMode.toMap());

        Iterable<ServiceSubtype> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);

        Iterable<ServiceProvider> sps = serviceProviderService.findAll();
        model.addAttribute("sps", sps);

        model.addAttribute("chargeWays", ChargeWay.values());

        model.addAttribute("taxRateList", taxRateService.findAll());
    }
}
