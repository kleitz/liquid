package liquid.operation.controller;

import liquid.operation.domain.TaxRate;
import liquid.operation.service.TaxRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

/**
 * Created by redbrick9 on 8/29/14.
 */
@Controller
@RequestMapping("/tax_rate")
public class TaxRateController {
    private static final Logger logger = LoggerFactory.getLogger(TaxRateController.class);

    @Autowired
    private TaxRateService taxRateService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        Collection<TaxRate> list = taxRateService.findAll();
        model.addAttribute("taxRateList", list);
        model.addAttribute("taxRate", new TaxRate());
        return "charge/tax_rate";
    }

    @RequestMapping(method = RequestMethod.POST, params = "add")
    public String addTaxRate(@ModelAttribute TaxRate taxRate) {
        taxRateService.save(taxRate);
        return "redirect:/tax_rate";
    }

    @RequestMapping(method = RequestMethod.POST, params = "disable")
    public String disable(@ModelAttribute TaxRate taxRate) {
        taxRateService.disable(taxRate.getId());
        return "redirect:/tax_rate";
    }

    @RequestMapping(method = RequestMethod.POST, params = "enable")
    public String enable(@ModelAttribute TaxRate taxRate) {
        taxRateService.enable(taxRate.getId());
        return "charge/tax_rate";
    }
}
