package liquid.controller;

import liquid.persistence.domain.CustomerEntity;
import liquid.persistence.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/24/13
 * Time: 11:26 PM
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private CustomerRepository customerRepository;

    @ModelAttribute("customers")
    public Iterable<CustomerEntity> populateCustomers() {
        return customerRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void list(Model model, Principal principal) {
        model.addAttribute("customer", new CustomerEntity());
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute CustomerEntity customer, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes, Model model, Principal principal) {
        logger.debug("Customer: {}", customer);

        if (bindingResult.hasErrors()) {
            return "customer";
        } else {
            customerRepository.save(customer);
            return "redirect:/customer";
        }
    }
}
