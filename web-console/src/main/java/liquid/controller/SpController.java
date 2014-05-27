package liquid.controller;

import liquid.persistence.domain.ServiceProviderEntity;
import liquid.service.ChargeService;
import liquid.service.ServiceProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 5:02 PM
 */
@Controller
@RequestMapping("/sp")
public class SpController {
    private static final Logger logger = LoggerFactory.getLogger(SpController.class);

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private ChargeService chargeService;

    @ModelAttribute("sps")
    public Iterable<ServiceProviderEntity> populateSps() {
        return serviceProviderService.findAll();
    }

    @ModelAttribute("spTypes")
    public Map<Long, String> populateSpTypes() {
        return serviceProviderService.getSpTypes();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model, Principal principal) {
        return "sp/sp";
    }

    @RequestMapping(method = RequestMethod.GET, params = "type")
    @ResponseBody
    public List<ServiceProviderEntity> list(@RequestParam long type) {
        logger.debug("type: {}", type);
//        long spType = spService.spTypeByChargeType((int) type);
//        return spService.findByType(spType);
        return serviceProviderService.findByChargeType(type);
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String initForm(Model model, Principal principal) {
        model.addAttribute("sp", new ServiceProviderEntity());
        return "sp/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("sp") ServiceProviderEntity sp,
                         BindingResult bindingResult, Model model, Principal principal) {
        logger.debug("sp: {}", sp);

        if (bindingResult.hasErrors()) {
            return "sp/sp";
        } else {
            serviceProviderService.save(sp);
            return "redirect:/sp";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable long id,
                           Model model, Principal principal) {
        logger.debug("id: {}", id);
        ServiceProviderEntity sp = serviceProviderService.find(id);
        model.addAttribute("sp", sp);
        model.addAttribute("cts", chargeService.getChargeTypes());
        return "sp/form";
    }
}