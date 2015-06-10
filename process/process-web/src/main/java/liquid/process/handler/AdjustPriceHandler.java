package liquid.process.handler;

import liquid.accounting.domain.Charge;
import liquid.accounting.domain.ChargeWay;
import liquid.accounting.service.ChargeService;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.ServiceSubtypeService;
import liquid.process.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * Created by Tao Ma on 6/8/15.
 */
@Component
public class AdjustPriceHandler extends AbstractTaskHandler {
    private static final Logger logger = LoggerFactory.getLogger(AdjustPriceHandler.class);

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @Autowired
    private ChargeService chargeService;

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void init(Task task, Model model) {
        // for charges
        Iterable<ServiceSubtype> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);
        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<Charge> charges = chargeService.findByTaskId(task.getId());
        model.addAttribute("charges", charges);
        model.addAttribute("chargesTotal", chargeService.total(charges));
    }

    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) { }
}
