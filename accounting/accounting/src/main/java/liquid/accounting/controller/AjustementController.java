package liquid.accounting.controller;

import liquid.accounting.domain.Charge;
import liquid.accounting.domain.ChargeWay;
import liquid.accounting.service.ChargeService;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.ServiceProviderService;
import liquid.operation.service.ServiceSubtypeService;
import liquid.process.controller.BaseTaskController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;

/**
 * User: tao
 * Date: 10/30/13
 * Time: 1:29 PM
 */
@Controller
@RequestMapping("/task/{taskId}/ajustement")
public class AjustementController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(AjustementController.class);

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @ModelAttribute("chargeWays")
    public ChargeWay[] populateChargeWays() {
        return ChargeWay.values();
    }

    @ModelAttribute("sps")
    public Iterable<ServiceProvider> populateSps() {
        return serviceProviderService.findAll();
    }

    @RequestMapping(value = "/charge", method = RequestMethod.GET)
    public String initAddCharge(@PathVariable String taskId,
                                Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        Iterable<ServiceSubtype> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);
        model.addAttribute("charge", new Charge());
        return "order/add_charge";
    }

    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public String addCharge(@PathVariable String taskId,
                            @Valid Charge charge,
                            BindingResult result) {
        logger.debug("taskId: {}", taskId);
        logger.debug("charge: {}", charge);
        if (result.hasErrors()) {
            return "order/add_charge";
        }

        chargeService.save(charge);

        return "redirect:/task/" + taskId + "/ajustement";
    }

    @RequestMapping(value = "/charge/{chargeId}", method = RequestMethod.GET)
    public String delCharge(@PathVariable String taskId,
                            @PathVariable long chargeId,
                            Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        chargeService.removeCharge(chargeId);
        return "redirect:/task/" + taskId + "/ajustement";
    }
}
