package liquid.operation.controller;

import liquid.core.controller.BaseController;
import liquid.core.model.Pagination;
import liquid.core.model.SearchBarForm;
import liquid.operation.domain.Customer;
import liquid.operation.service.InternalCustomerService;
import liquid.pinyin4j.PinyinHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * User: tao
 * Date: 9/24/13
 * Time: 11:26 PM
 */
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private InternalCustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(SearchBarForm searchBarForm, Model model, HttpServletRequest request) {
        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<Customer> page = customerService.findAll(searchBarForm.getText(), pageRequest);

        model.addAttribute("page", page);
        searchBarForm.prepand(request.getRequestURI());
        return "customer/page";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String initForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute Customer customer, BindingResult bindingResult) {
        logger.debug("Customer: {}", customer);

        if (bindingResult.hasErrors()) {
            return "customer/form";
        } else {
            if (null == customer.getId()) {
                String queryName = PinyinHelper.converterToFirstSpell(customer.getName()) + ";" + customer.getName();
                customer.setQueryName(queryName);
            }
            customerService.save(customer);
            return "redirect:/customer";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);
        Customer customer = customerService.find(id);
        model.addAttribute("customer", customer);
        return "customer/form";
    }
}
