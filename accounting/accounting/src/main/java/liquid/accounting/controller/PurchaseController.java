package liquid.accounting.controller;

import liquid.accounting.domain.Purchase;
import liquid.accounting.service.PurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by mat on 3/27/16.
 */
@Controller
@RequestMapping("/purchase")
public class PurchaseController {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    private PurchaseService purchaseService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String addPurchase(Purchase purchase,
                              @RequestHeader(value = "referer") String referer,
                              BindingResult bindingResult, Model model) {
        logger.debug("Purchase: {}", purchase);
        purchaseService.save(purchase);
        return "redirect:" + referer;
    }
}
