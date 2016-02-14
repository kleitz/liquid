package liquid.operation.controller;

import liquid.core.controller.BaseController;
import liquid.core.model.Pagination;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.service.InternalServiceProviderService;
import liquid.operation.service.ServiceProviderTypeService;
import liquid.operation.service.ServiceSubtypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * User: tao
 * Date: 10/2/13
 * Time: 5:02 PM
 */
@Controller
@RequestMapping("/sp")
public class ServiceProviderController extends BaseController {
    private final static String ROOT_DIR = "operation/sp/";

    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderController.class);

    @Autowired
    private InternalServiceProviderService serviceProviderService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @Autowired
    private ServiceProviderTypeService serviceProviderTypeService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Pagination pagination, Model model, HttpServletRequest request) {
        PageRequest pageRequest = new PageRequest(pagination.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<ServiceProvider> page = serviceProviderService.findAll(pageRequest);

        pagination.prepand(request.getRequestURI());
        model.addAttribute("page", page);
        return ROOT_DIR + "list";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String initForm(Model model) {
        model.addAttribute("spTypes", serviceProviderTypeService.findAll());
        model.addAttribute("serviceSubtypes", serviceSubtypeService.findEnabled());

        model.addAttribute("sp", new ServiceProvider());
        return ROOT_DIR + "form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        model.addAttribute("spTypes", serviceProviderTypeService.findAll());
        model.addAttribute("serviceSubtypes", serviceSubtypeService.findEnabled());

        ServiceProvider sp = serviceProviderService.find(id);
        model.addAttribute("sp", sp);
        return ROOT_DIR + "form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("sp") ServiceProvider sp, BindingResult bindingResult) {
        logger.debug("sp: {}", sp);

        if (bindingResult.hasErrors()) {
            return ROOT_DIR + "sp";
        } else {
            sp.setStatus(0);
            serviceProviderService.save(sp);
            return "redirect:/sp";
        }
    }
}