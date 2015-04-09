package liquid.accounting.controller;

import liquid.accounting.facade.ReceiptFacade;
import liquid.accounting.model.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Tao Ma on 1/9/15.
 */
@Controller
@RequestMapping("/receipt")
public class ReceiptController {
    @Autowired
    private ReceiptFacade receiptFacade;

    @RequestMapping(method = RequestMethod.POST)
    public String add(@ModelAttribute(value = "receipt") Receipt receipt,
                      @RequestHeader(value = "referer", required = false) final String referer,
                      BindingResult bindingResult) {
        receiptFacade.save(receipt);
        return "redirect:" + referer;
    }
}
